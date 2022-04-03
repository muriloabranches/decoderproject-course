package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid ModuleDto moduleDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);

        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());

        moduleService.save(moduleModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleModel);
    }

    @DeleteMapping("{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(moduleId, courseId);

        if (!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
        }

        moduleService.delete(moduleModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Module successfully deleted!");
    }

    @PutMapping("{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(moduleId, courseId);

        if (!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
        }

        var moduleModel = moduleModelOptional.get();
        BeanUtils.copyProperties(moduleDto, moduleModel);

        moduleService.save(moduleModel);

        return ResponseEntity.status(HttpStatus.OK).body(moduleModel);
    }

    @GetMapping
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(courseId));
    }

    @GetMapping("{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(moduleId, courseId);

        if (!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
    }
}