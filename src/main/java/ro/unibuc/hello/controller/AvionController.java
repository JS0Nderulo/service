package ro.unibuc.hello.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.InfoAvion;
import ro.unibuc.hello.data.Avion;
import ro.unibuc.hello.exception.DuplicateException;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.NullOrEmptyNumberException;
import ro.unibuc.hello.service.AvionService;

import java.util.List;

import java.util.concurrent.atomic.AtomicLong;
@Controller
public class AvionController {

    @Autowired
    private AvionService avionService;

    private static final String duplicateExceptionMessage = "An avion entity with the same number already exists so the state of the DB wasn't modified.";
    private static final String entityNotFoundExceptionMessage = "Avion entity with the requested number was not found so the state of the DB wasn't modified.";
    private static final String nullOrEmptyNumberExceptionMessage = "The provided number for the Avion entity is null or empty so the state of the DB wasn't modified.";

    @GetMapping("/avion/{number}")
    @ResponseBody
    @Timed(value = "avion.getAvion.time", description = "Time taken to return infoAvion entity from getAvion request")
    @Counted(value = "avion.getAvion.count", description = "Times infoAvion entities were returned from getAvion requests")
    public ResponseEntity<?> getAvion(@PathVariable("number") String number)  {
        try {
            return ResponseEntity.ok().body(avionService.getAvionInfoByNumber(number));
        }
        catch (EntityNotFoundException exception) {
            return ResponseEntity.ok().body("Avion entity with the requested number not found.");
        }
    }

    @GetMapping("/avion")
    @ResponseBody
    @Timed(value = "avion.getAllAvioane.time", description = "Time taken to return infoAvion entities from getAllAvioane request")
    @Counted(value = "avion.getAllAvioane.count", description = "Times infoAvion entities were returned from getAllAvioane requests")
    public ResponseEntity<List<InfoAvion>> getAllAvioane() {
        return ResponseEntity.ok().body(avionService.getAllAvioane());
    }

    @PostMapping("/avion")
    @ResponseBody
    @Timed(value = "avion.addAvion.time", description = "Time taken to return the response from addAvion request")
    @Counted(value = "avion.addAvion.count", description = "Times responses were returned from addAvion requests")
    public ResponseEntity<?>  addAvion(@RequestBody Avion avion) {
        try {
            InfoAvion newAvion=avionService.addAvion(avion);
            return ResponseEntity.ok().body(newAvion);
        }
        catch (DuplicateException exception) {
            return ResponseEntity.ok().body(duplicateExceptionMessage);
        }
        catch (NullOrEmptyNumberException exception) {
            return ResponseEntity.ok().body(nullOrEmptyNumberExceptionMessage);
        }
    }

    @DeleteMapping("/avion/{number}")
    @ResponseBody
    @Timed(value = "avion.removeAvion.time", description = "Time taken to return the response from removeAvion request")
    @Counted(value = "avion.removeAvion.count", description = "Times responses were returned from removeAvion requests")
    public ResponseEntity<?> removeAvion(@PathVariable("number") String number)  {
        try {
            return ResponseEntity.ok().body(avionService.removeAvion(number));
        }
        catch (EntityNotFoundException exception) {
            return ResponseEntity.ok().body(entityNotFoundExceptionMessage);
        }
    }

    @PutMapping("/avion/{number}")
    @ResponseBody
    @Timed(value = "avion.updateAvion.time", description = "Time taken to return the response from updateAvion request")
    @Counted(value = "avion.updateAvion.count", description = "Times responses were returned from updateAvion requests")
    public ResponseEntity<?> updateAvion(@PathVariable("number") String number, @RequestBody Avion avion) {
        try {
            return ResponseEntity.ok().body(avionService.updateAvion(number, avion));
        }
        catch (EntityNotFoundException exception) {
            return ResponseEntity.ok().body(entityNotFoundExceptionMessage);
        }
        catch (DuplicateException exception) {
            return ResponseEntity.ok().body(duplicateExceptionMessage);
        }
        catch (NullOrEmptyNumberException exception) {
            return ResponseEntity.ok().body(nullOrEmptyNumberExceptionMessage);
        }
    }


    @GetMapping("/avionfilter")
    @ResponseBody
    @Timed(value = "avion.getAvioaneByProperties.time", description = "Time taken to return the response from getAvioaneByProperties request")
    @Counted(value = "avion.getAvioaneByProperties.count", description = "Times responses were returned from getAvioaneByProperties requests")
    public ResponseEntity<?> getAvioaneByProperties(@RequestParam(name = "from",required = false) String from, @RequestParam(name = "to",required = false) String to) {
        return ResponseEntity.ok().body(avionService.fetchAvionByProperty(from, to));
    }
}

