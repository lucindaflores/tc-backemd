package be.vdab.tcbackend.products;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("materials")
class MaterialController {

    private final MaterialService materialService;

    MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    private record MaterialDetails(
            long materialId,
            String name,
            String nameSpanish,
            String technique) {

        MaterialDetails(Material material) {
            this(material.getId(),
                 material.getName(),
                 material.getNameSpanish(),
                 material.getTechnique()
            );
        }
    }


    @GetMapping
    List<MaterialDetails> findAll() {
        return materialService.findAll()
                .stream()
                .map(MaterialDetails::new)
                .toList();
    }

    @GetMapping("{id}")
    MaterialDetails findById(@PathVariable long id) {
        return materialService.findBy(id)
                .map(MaterialDetails::new)
                .orElseThrow(MaterialNotFoundException::new);
    }

    @GetMapping("byName")
    MaterialDetails findByName(@RequestParam String name) {
        return materialService.findByName(name)
                .map(MaterialDetails::new)
                .orElseThrow(MaterialNotFoundException::new);
    }

    @PostMapping
    long create(@RequestBody @Valid NewMaterial newMaterial) {
        return materialService.create(newMaterial);
    }

    @PutMapping("{id}")
    void update(@PathVariable long id,
                @RequestBody @Valid NewMaterial newMaterial) {
        materialService.update(id, newMaterial);
    }

    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        materialService.delete(id);
    }
}
