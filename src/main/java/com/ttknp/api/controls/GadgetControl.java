package com.ttknp.api.controls;

import com.ttknp.api.entities.FileType;
import com.ttknp.api.entities.Gadget;
import com.ttknp.api.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping(value = "/gadget")
public class GadgetControl {

    private final ModelService<Gadget> modelService;

    @Autowired
    public GadgetControl(ModelService<Gadget> modelService) {
        this.modelService = modelService;
    }

    @GetMapping(value = "/selectAll")
    private ResponseEntity<List<Gadget>> selectAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelService.getAll());
    }

    @GetMapping(value = "/selectAllByBrand")
    private ResponseEntity<List<Gadget>> selectAllByBrand(@RequestParam String value) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelService.getAllByKey(value));
    }

    @GetMapping(value = "/selectColumnAllByBrand")
    private ResponseEntity<List<String>> selectColumnAllByBrand() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelService.getColumnByKey("brand"));
    }

    @GetMapping(value = "/selectByGid")
    private ResponseEntity<Gadget> selectByGid(@RequestParam String value) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelService.getById(value));
    }

    @PostMapping(value = "/insert")
    private ResponseEntity<Boolean> insert(@RequestBody Gadget gadget) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelService.add(gadget));
    }

    @PutMapping(value = "/update")
    private ResponseEntity<Boolean> update(@RequestBody Gadget gadget) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(modelService.update(gadget));
    }

    @DeleteMapping(value = "/delete/{gid}")
    private ResponseEntity<Boolean> deleteModel(@PathVariable String gid) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(modelService.deleteById(gid));
    }

    @PostMapping("/report")
    private ResponseEntity<Resource> reportAuth(@RequestBody FileType fileType)  {
        HashMap<String,byte[]> map = modelService.getGadgetHasMapReport(fileType.getFileExtension());
        if (!map.isEmpty()) {
            List<String> keySet = map.keySet().stream().toList();
            ByteArrayResource resource = new ByteArrayResource(map.get(keySet.get(0)));
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment; filename=\"" + keySet.get(0) + "\"")
                    .header("File-Name", keySet.get(0)) // Note, custom headers won't work until you  .cors(cors -> cors.configurationSource(corsConfigurationSource())) on filterChain
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        else {
            throw new RuntimeException("File Download Failed");
        }
    }

    @PostMapping("/reportByBrand")
    private ResponseEntity<Resource> reportByBrandAuth(@RequestBody FileType fileType, @RequestParam String value)  {
        HashMap<String,byte[]> map = modelService.getGadgetHasMapReport(fileType.getFileExtension(),value);
        if (!map.isEmpty()) {
            List<String> keySet = map.keySet().stream().toList();
            ByteArrayResource resource = new ByteArrayResource(map.get(keySet.get(0)));
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, "attachment; filename=\"" + keySet.get(0) + "\"")
                    .header("File-Name", keySet.get(0)) // Note, custom headers won't work until you  .cors(cors -> cors.configurationSource(corsConfigurationSource())) on filterChain
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        else {
            throw new RuntimeException("File Download Failed");
        }
    }
}
