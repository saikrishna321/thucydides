package net.thucydides.easyb.samples.pages;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import net.thucydides.easyb.samples.pages.NestedSteps;

public class DemoSiteSteps extends ScenarioSteps {

    public DemoSiteSteps(Pages pages) {
        super(pages);
    }

    @Steps
    public NestedSteps nestedSteps;

    @Step
    public void enter_values(String selectValue, boolean checkboxValue) {
        IndexPage page = (IndexPage) getPages().currentPageAt(IndexPage.class);
        page.selectItem(selectValue);
        page.setCheckboxOption(checkboxValue);
    }

    @Step
    public void call_nested_steps(String selectValue, boolean checkboxValue) {
        nestedSteps.enter_values(selectValue, checkboxValue);
        nestedSteps.should_display(selectValue);
    }

    @Step
    public void fields_should_be_displayed() {
        IndexPage page = (IndexPage) getPages().currentPageAt(IndexPage.class);
        page.shouldBeVisible(page.multiselect);
    }

    @Step
    public void should_display(String selectValue) {
        IndexPage page = (IndexPage) getPages().currentPageAt(IndexPage.class);
        page.shouldContainText(selectValue);
    }

    @Step
    public void should_have_selected_value(String selectValue) {
        IndexPage page = (IndexPage) getPages().currentPageAt(IndexPage.class);
        if (!page.getSelectedValues().contains(selectValue)) {
            throw new AssertionError("Value $selectValue not in $page.selectedValues");
        }
    }

    @Step
    public void should_not_have_selected_value(String selectValue) {
        IndexPage page = (IndexPage) getPages().currentPageAt(IndexPage.class);
        if (page.getSelectedValues().contains(selectValue)) {
            throw new AssertionError();
        }
    }


    @Step
    public void trigger_exception() {
        throw new AssertionError("It broke");
    }

}