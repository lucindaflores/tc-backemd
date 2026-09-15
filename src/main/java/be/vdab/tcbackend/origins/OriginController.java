package be.vdab.tcbackend.origins;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("origins")
class OriginController {

    private final OriginService originService;

    OriginController(OriginService originService) {
        this.originService = originService;
    }

    /* DTOs */
    private record OriginName(long id, String name) {
        OriginName(Origin origin) {
            this(origin.getId(), origin.getName());
        }
    }

    private record UpdateName(@NotBlank String name) { }


    /* REQUESTS */
    @GetMapping
    List<OriginName> findAll() {
        return originService.findAll()
                .stream()
                .map(OriginName::new)
                .toList();
    }

    @GetMapping("{id}")
    OriginName findById(@PathVariable long id) {
        return originService.findBy(id)
                .map(OriginName::new)
                .orElseThrow(OriginNotFoundException::new);
    }

    @GetMapping("byName")
    OriginName findByName(@RequestParam String name) {
        return originService.findByName(name)
                .map(OriginName::new)
                .orElseThrow(OriginNotFoundException::new);
    }

    @PostMapping
    long create(@RequestBody @Valid NewOrigin newOrigin) {
        return originService.create(newOrigin);
    }

    @PutMapping("{id}/name")
    void updateName(@PathVariable long id,
                    @RequestBody @Valid UpdateName updateName) {
        originService.updateName(id, updateName.name());
    }

    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        originService.delete(id);
    }

}

