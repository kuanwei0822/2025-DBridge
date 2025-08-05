package com.project.api.controller;

import com.project.extractor.model.PostgreTableMeta;
import com.project.service.PostgreMetadataExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/metadata")
@RequiredArgsConstructor
public class MetadataPageController {

    private final PostgreMetadataExtractionService service;

    @GetMapping
    public String showTableList(Model model) {
        List<PostgreTableMeta> tables = service.extractTables();
        model.addAttribute("tables", tables);
        return "metadata/list"; // resources/templates/metadata/list.html
    }

    @GetMapping("/detail")
    public String showTableDetail(@RequestParam("tableName") String tableName, Model model) {
        List<PostgreTableMeta> tables = service.extractTables();
        PostgreTableMeta selected = tables.stream()
                .filter(t -> t.getTableName().equals(tableName))
                .findFirst()
                .orElse(null);

        model.addAttribute("table", selected);
        return "metadata/detail"; // resources/templates/metadata/detail.html
    }
}
