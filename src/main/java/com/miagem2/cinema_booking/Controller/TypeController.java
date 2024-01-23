package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.Model.Type;
import com.miagem2.cinema_booking.Repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/types")
public class TypeController {

    @Autowired
    private TypeRepository typeRepository;

    @GetMapping
    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    @PostMapping
    public Type createType(@RequestBody Type type) {
        return typeRepository.save(type);
    }

    @GetMapping("/{typeId}")
    public Type getTypeById(@PathVariable Long typeId) {
        return typeRepository.findById(typeId).orElse(null);
    }

    @PutMapping("/{typeId}")
    public Type updateType(@PathVariable Long typeId, @RequestBody Type typeDetails) {
        Type type = typeRepository.findById(typeId).orElse(null);

        if (type != null) {
            // Mettez à jour les détails du type
            type.setName(typeDetails.getName());
            type.setPrice(typeDetails.getPrice());

            return typeRepository.save(type);
        }

        return null; // Gérer le cas où le type n'est pas trouvé
    }

    @DeleteMapping("/{typeId}")
    public void deleteType(@PathVariable Long typeId) {
        typeRepository.deleteById(typeId);
    }
}
