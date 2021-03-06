package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.service.GeoNamesService;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author lferreira
 * @since 5/7/18 6:53 PM
 */
@Controller
@RequestMapping("/projects/{projectId}/holidays")
public class HolidayController extends AbstractController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GeoNamesService geoNamesService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long projectId,
                              @PageableDefault(sort = "date") final Pageable pageable) {
        final Page<Holiday> holidays = holidayService.findByProject(projectId, pageable);
        final Project project = projectService.findById(projectId);
        return new ModelAndView("holidays/index")
                .addObject("holidays", holidays)
                .addObject("states", geoNamesService.findAllStatesOfBrazil().getGeonames())
                .addObject("project", project);
    }

    @GetMapping("/new")
    public ModelAndView create(@PathVariable final Long projectId) {
        return new ModelAndView("holidays/new")
                .addObject("holiday", new Holiday())
                .addObject("projectId", projectId);
    }

    @PostMapping
    public ModelAndView create(@PathVariable final Long projectId,
                               @Validated final Holiday holiday,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("holidays/new")
                    .addObject("holiday", holiday)
                    .addObject("projectId", projectId);
        }

        holidayService.create(projectId, holiday);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro inserido com sucesso");
        return new ModelAndView("redirect:/projects/" + projectId + "/holidays");
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable final Long projectId,
                               @PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        holidayService.delete(id);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro removido com sucesso");
        return new ModelAndView("redirect:/projects/" + projectId + "/holidays");
    }

    @PostMapping("/import")
    public ModelAndView importFromAPI(@PathVariable final Long projectId,
                                      @RequestParam("selectCity") final String city, final RedirectAttributes redirectAttributes) {

        if (holidayService.createImported(projectId, city)) {
            redirectAttributes.addFlashAttribute("flashSuccess", "Registros importados com sucesso");
        } else {
            redirectAttributes.addFlashAttribute("flashError", "Feriados ja importados");
        }

        return new ModelAndView("redirect:/projects/" + projectId + "/holidays");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView update(@PathVariable final Long projectId, @PathVariable final Long id) {
        Holiday holiday = holidayService.findById(id);
        return new ModelAndView("holidays/edit")
                .addObject("holiday", holiday)
                .addObject("projectId", projectId);
    }

    @PutMapping
    public ModelAndView update(@PathVariable final Long projectId,
                               @Validated final Holiday holiday,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("holidays/edit")
                    .addObject("holiday", holiday)
                    .addObject("projectId", projectId);
        }

        holidayService.update(projectId, holiday);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro atualizado com sucesso");
        return new ModelAndView("redirect:/projects/" + projectId + "/holidays");
    }

}
