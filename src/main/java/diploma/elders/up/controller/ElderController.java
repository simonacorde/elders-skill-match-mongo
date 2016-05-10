package diploma.elders.up.controller;

import diploma.elders.up.dao.documents.OptimizationResult;
import diploma.elders.up.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.ExecutionException;

/**
 * Created by Simonas on 5/10/2016.
 */
@Controller
public class ElderController {

    @Autowired
    private MatchingService matchingService;

    @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/")
    public String homeIndex() {
        return "home";
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/match", method = RequestMethod.POST)
    public String match(@ModelAttribute("size") String size, ModelMap model) throws ExecutionException, InterruptedException {
        OptimizationResult optimizationResult = matchingService.applyMatchingAlgorithm(Integer.parseInt(size), "56faa8ceecdbd7fb897772c6");
        model.addAttribute("result", optimizationResult);
        return "hello";
    }


}
