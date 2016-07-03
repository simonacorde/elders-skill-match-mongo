package diploma.elders.up.controller;

import diploma.elders.up.NotFoundException;
import diploma.elders.up.ValidationException;
import diploma.elders.up.dao.documents.Opportunity;
import diploma.elders.up.dao.documents.OptimizationResult;
import diploma.elders.up.dao.documents.Senior;
import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by Simonas on 5/10/2016.
 */
@Controller
public class ElderController {

    @Autowired
    private MatchingService matchingService;
    @Autowired
    private OpportunityRepository opportunityRepository;

    @RequestMapping(value = "/")
    public String homeIndex() {
        return "hello";
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/back", method = RequestMethod.POST)
    public String back() {
        return "hello";
    }

    @RequestMapping(value = "/match", method = RequestMethod.POST)
    public String match(@ModelAttribute("size") String size, @ModelAttribute("offerId") String offerId, ModelMap model) throws ExecutionException, InterruptedException, NotFoundException, ValidationException {
        OptimizationResult optimizationResult = matchingService.applyMatchingAlgorithm(Integer.parseInt(size), offerId);
        model.addAttribute("score", optimizationResult.getMatchingScore());
        model.addAttribute("skill", new Skill());
        model.addAttribute("elder", new Senior());
        model.addAttribute("elders", optimizationResult.getElders().stream().map(ElderDTO::getElder).collect(Collectors.toList()));
        return "match";
    }

    @RequestMapping(value = "/offers", method = RequestMethod.GET)
    public String listOffers( ModelMap model) {
        List<Opportunity> opportunities = matchingService.getAvailableOpportunites();
        model.addAttribute("offer", new Opportunity());
        model.addAttribute("skill", new Skill());
        model.addAttribute("offer", new Opportunity());
        model.addAttribute("offers", opportunities);
        return "hello";
    }

}
