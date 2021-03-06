package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors,
                                       Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }

    @RequestMapping(value = "category/{categoryId}", method = RequestMethod.GET)
    public String category(Model model,
                           @PathVariable int categoryId) {
        Category cat = categoryDao.findOne(categoryId);

        model.addAttribute("cheeses",cat.getCheeses());
        model.addAttribute("title","Cheeses in category: " + cat.getName());

        return "cheese/index";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model,
                                  @PathVariable int cheeseId) {
        Cheese cheese = cheeseDao.findOne(cheeseId);

        model.addAttribute("cheese",cheese);
        model.addAttribute("categories",categoryDao.findAll());
        model.addAttribute("title","Edit Cheese: " + cheese.getName());

        return "cheese/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(Model model,
                                  @RequestParam int cheeseId,
                                  @ModelAttribute @Valid Cheese formCheese,
                                  Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("cheese", formCheese);
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute("title", "Edit Cheese: " + formCheese.getName());

            return "cheese/edit";
        }

        Cheese cheese = cheeseDao.findOne(cheeseId);
        cheese.setName(formCheese.getName());
        cheese.setDescription(formCheese.getDescription());
        cheese.setCategory(formCheese.getCategory());
        cheeseDao.save(cheese);

        return "redirect:";
    }

}
