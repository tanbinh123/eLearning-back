package com.example.demo.controllers;

import com.example.demo.dto.NastavnikDto;
import com.example.demo.model.Korisnik;
import com.example.demo.model.Mesto;
import com.example.demo.model.Nastavnik;
import com.example.demo.model.NastavnikTip;
import com.example.demo.services.KorisnikService;
import com.example.demo.services.MestoService;
import com.example.demo.services.NastavnikTipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/nastavnik")
public class NastavnikController {

    private final KorisnikService<Korisnik> korisnikService;
    private final MestoService mestoService;
    private final NastavnikTipService nastavnikTipService;

    public NastavnikController(KorisnikService<Korisnik> korisnikService,
                               MestoService mestoService,
                               NastavnikTipService nastavnikTipService) {
        this.korisnikService = korisnikService;
        this.mestoService = mestoService;
        this.nastavnikTipService = nastavnikTipService;
    }

    @GetMapping
    public ResponseEntity<?> getNastavnici() {
        List<Nastavnik> nastavnici = korisnikService.getAllNastavnici();
        return new ResponseEntity<>(nastavnici, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNastavnikById(@PathVariable("id") long id) {
        Nastavnik nastavnik = (Nastavnik) korisnikService.getById(id);
        if (nastavnik == null) {
            return new ResponseEntity<>("Nastavnik not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(nastavnik, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addNastavnik(@RequestBody NastavnikDto nastavnikDto) {
        Mesto mesto = mestoService.getById(nastavnikDto.getMestoId());
        NastavnikTip nastavnikTip = nastavnikTipService.getById(nastavnikDto.getNastavnikTipId());
        Nastavnik nastavnik = new Nastavnik(nastavnikDto, mesto, nastavnikTip);
        korisnikService.add(nastavnik);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editNastavnik(@PathVariable("id") long id,
                                           @RequestBody NastavnikDto nastavnikDto) {
        Nastavnik nastavnik = (Nastavnik) korisnikService.getById(id);
        Mesto mesto = mestoService.getById(nastavnikDto.getMestoId());
        NastavnikTip nastavnikTip = nastavnikTipService.getById(nastavnikDto.getNastavnikTipId());
        if (nastavnik == null || mesto == null || nastavnikTip == null) {
            return new ResponseEntity<>("Nastavnik, Mesto or NastavnikTip not found.", HttpStatus.NOT_FOUND);
        }
        Nastavnik nastavnikDb = (Nastavnik) korisnikService.save(nastavnik.update(nastavnikDto, mesto, nastavnikTip));
        nastavnikDto = new NastavnikDto(nastavnikDb.getId(), nastavnikDb);
        return new ResponseEntity<>(nastavnikDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNastavnik(@PathVariable("id") long id) {
        Nastavnik nastavnik = (Nastavnik) korisnikService.getById(id);
        if (nastavnik == null) {
            return new ResponseEntity<>("Nastavnik not found.", HttpStatus.NOT_FOUND);
        }
        korisnikService.delete(nastavnik);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
