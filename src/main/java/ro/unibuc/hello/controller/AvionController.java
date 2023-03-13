package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.InfoAvion;
import ro.unibuc.hello.data.Avion;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.AvionService;

@Controller
public class AvionController {

    @Autowired
    private AvionService avionService;

    @GetMapping("/avion")
    @ResponseBody
    public InfoAvion getAvion(@RequestParam(name = "number", required = false, defaultValue = "1") String number) throws EntityNotFoundException {
        return avionService.getAvionInfoByNumber(number);
    }

    @PostMapping("/avion")
    @ResponseBody
    public InfoAvion addAvion(@RequestBody Avion avion) throws EntityNotFoundException {
        InfoAvion newAvion=avionService.addAvion(avion);
        return newAvion;
    }

    @DeleteMapping("/avion/{number}")
    @ResponseBody
    public void removeAvion(@PathVariable("number") String number) throws EntityNotFoundException {
        avionService.removeAvion(number);
    }

    @PutMapping("/avion/{number}")
    @ResponseBody
    public InfoAvion updateAvion(@PathVariable("number") String number, @RequestBody Avion avion) throws EntityNotFoundException {
        return avionService.updateAvion(number,avion);
    }

    @GetMapping("/avionfilter")
    public ResponseEntity<?> getAvioaneByProperties(@RequestParam(name = "from",required = false) String from, @RequestParam(name = "to",required = false) String to) {
        return ResponseEntity.ok().body(avionService.fetchAvionByProperty(from, to));
    }
}

