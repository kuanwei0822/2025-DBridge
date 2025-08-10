package com.project.api.controller;

import com.project.service.GenericMongoService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/mongo")
public class GenericMongoController {

    private final GenericMongoService service;

    public GenericMongoController(GenericMongoService service) {
        this.service = service;
    }

    // 首頁：列出 DB 與（選擇後）Collections
    @GetMapping
    public String index(@RequestParam(value = "db", required = false) String db,
                        Model model) {
        model.addAttribute("databases", service.listDatabases());
        model.addAttribute("db", db);
        if (db != null && !db.isBlank()) {
            model.addAttribute("collections", service.listCollections(db));
        }
        return "mongo/index";
    }

    // 查詢：以 JSON 字串參數建立通用查詢，並將結果轉為可序列化結構
    @GetMapping("/query")
    public String query(@RequestParam String db,
                        @RequestParam String coll,
                        @RequestParam(required = false) String filter,     // {"price":{"$gte":1000}}
                        @RequestParam(required = false) String projection, // {"name":1,"price":1}
                        @RequestParam(required = false) String sort,       // {"_id":1} / {"price":-1}
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "20") int size,
                        Model model) {

        Document f = parseJson(filter, "filter", model);
        Document p = parseJson(projection, "projection", model);
        Document s = parseJson(sort, "sort", model);

        long total = service.count(db, coll, f);
        List<Document> docs = service.find(db, coll, f, p, s, page, size);

        // 轉成可序列化 Map（含遞迴處理 ObjectId/Date/List/Document）
        List<Map<String,Object>> rows = new ArrayList<>();
        for (Document d : docs) rows.add(serializeBson(d));

        model.addAttribute("databases", service.listDatabases());
        model.addAttribute("collections", service.listCollections(db));
        model.addAttribute("db", db);
        model.addAttribute("coll", coll);
        model.addAttribute("filter", defaultIfBlank(filter, "{}"));
        model.addAttribute("projection", defaultIfBlank(projection, "{}"));
        model.addAttribute("sort", defaultIfBlank(sort, "{\"_id\":1}"));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("total", total);
        model.addAttribute("pages", (total + Math.max(size,1) - 1) / Math.max(size,1));
        model.addAttribute("rows", rows); // 前端以 JSON 使用

        return "mongo/query-tree";
    }

    private static Document parseJson(String json, String fieldName, Model model) {
        if (json == null || json.isBlank()) return null;
        try { return Document.parse(json); }
        catch (Exception e) {
            model.addAttribute("error_" + fieldName, "Invalid JSON: " + e.getMessage());
            return new Document();
        }
    }
    private static String defaultIfBlank(String s, String def) {
        return (s == null || s.isBlank()) ? def : s;
    }

    // --- BSON → 可序列化 Java 值 ---
    @SuppressWarnings("unchecked")
    private static Object toSerializable(Object v) {
        if (v == null) return null;
        if (v instanceof ObjectId oid) return oid.toHexString();         // 顯示為 24 位 hex
        if (v instanceof Date d)       return d.toInstant();             // ISO-8601
        if (v instanceof Instant i)    return i.toString();
        if (v instanceof Document doc) return serializeBson(doc);
        if (v instanceof Map<?,?> m) {
            Map<String,Object> out = new LinkedHashMap<>();
            m.forEach((k,val) -> out.put(String.valueOf(k), toSerializable(val)));
            return out;
        }
        if (v instanceof List<?> list) {
            List<Object> out = new ArrayList<>(list.size());
            for (Object e : list) out.add(toSerializable(e));
            return out;
        }
        return v; // String/Number/Boolean 直接返回
    }
    private static Map<String,Object> serializeBson(Document d) {
        Map<String,Object> out = new LinkedHashMap<>();
        for (Map.Entry<String,Object> e : d.entrySet()) {
            out.put(e.getKey(), toSerializable(e.getValue()));
        }
        return out;
    }
}
