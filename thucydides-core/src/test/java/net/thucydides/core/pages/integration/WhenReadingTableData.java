package net.thucydides.core.pages.integration;


import net.thucydides.core.pages.components.HtmlTable;
import net.thucydides.core.webdriver.WebDriverFacade;
import net.thucydides.core.webdriver.WebDriverFactory;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;
import java.util.Map;

import static net.thucydides.core.matchers.BeanMatchers.the;
import static net.thucydides.core.pages.components.HtmlTable.inTable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class WhenReadingTableData extends FluentElementAPITestsBaseClass {

    WebDriver htmlUnitDriver;
    StaticSitePage page;

    @Before
    public void openStaticPage() {
        htmlUnitDriver = new WebDriverFacade(HtmlUnitDriver.class, new WebDriverFactory());
        page = new StaticSitePage(htmlUnitDriver, 1);
        page.setWaitForTimeout(500);
        page.open();
    }



    @Test
    public void should_read_table_headings() {
        HtmlTable table = new HtmlTable(page.clients);
        List<String> tableHeadings = table.getHeadings();
        assertThat(tableHeadings.toString(), is("[First Name, Last Name, Favorite Colour]"));
    }

    @Test
    public void should_read_table_data_as_a_list_of_maps() {
        HtmlTable table = new HtmlTable(page.clients);

        List<Map<Object, String>> tableRows = table.getRows();
        assertThat(tableRows.size(), is(3));


        assertThat(tableRows.get(0), allOf(hasEntry("First Name", "Tim"), hasEntry("Last Name", "Brooke-Taylor"), hasEntry("Favorite Colour", "Red")));
        assertThat(tableRows.get(1), allOf(hasEntry("First Name", "Graeme"), hasEntry("Last Name", "Garden"), hasEntry("Favorite Colour", "Green")));
        assertThat(tableRows.get(2), allOf(hasEntry("First Name", "Bill"),hasEntry("Last Name", "Oddie"), hasEntry("Favorite Colour","Blue")));
    }


    @Test
    public void should_read_table_data_as_a_list_of_web_elements() {
        HtmlTable table = new HtmlTable(page.clients);

        List<WebElement> tableRows = table.getRowElements();
        assertThat(tableRows.size(), is(3));
    }

    private org.hamcrest.Matcher<java.util.Map<Object, String>> hasEntry(Object key, String value) {
        return new TableRowMatcher(key, value);
    }

    private class TableRowMatcher extends TypeSafeMatcher<Map<Object, String>> {

        private final Object key;
        private final String expectedValue;
        
        private TableRowMatcher(Object key, String expectedValue) {
            this.key = key;
            this.expectedValue = expectedValue;
        }

        @Override
        public boolean matchesSafely(Map<Object, String> map) {
            String actualValue = map.get(key);
            return (StringUtils.equals(expectedValue, actualValue));
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" A map containing an entry " + key + " => " + expectedValue);
        }
    };

    @Test
    public void should_be_able_to_also_access_rows_by_column_number() {
        HtmlTable table = new HtmlTable(page.clients);

        List<Map<Object, String>> tableRows = table.getRows();

        assertThat(tableRows.size(), is(3));
        assertThat(tableRows.get(0), allOf(hasEntry(1, "Tim"), hasEntry(2, "Brooke-Taylor"), hasEntry(3, "Red")));
        assertThat(tableRows.get(1), allOf(hasEntry(1, "Graeme"), hasEntry(2, "Garden"), hasEntry(3, "Green")));
        assertThat(tableRows.get(2), allOf(hasEntry(1, "Bill"),hasEntry(2, "Oddie"), hasEntry(3,"Blue")));
    }

    @Test
    public void should_read_table_data_using_a_static_method() {
        HtmlTable table = new HtmlTable(page.clients);

        List<Map<Object, String>> tableRows = HtmlTable.rowsFrom(page.clients);

        assertThat(tableRows.size(), is(3));
        assertThat(tableRows.get(0), allOf(hasEntry("First Name", "Tim"), hasEntry("Last Name", "Brooke-Taylor"), hasEntry("Favorite Colour", "Red")));
        assertThat(tableRows.get(1), allOf(hasEntry("First Name", "Graeme"), hasEntry("Last Name", "Garden"), hasEntry("Favorite Colour", "Green")));
        assertThat(tableRows.get(2), allOf(hasEntry("First Name", "Bill"),hasEntry("Last Name", "Oddie"), hasEntry("Favorite Colour","Blue")));
    }


    @Test
    public void should_ignore_data_in_extra_cells() {
        HtmlTable table = new HtmlTable(page.clients_with_extra_cells);

        List<Map<Object, String>> tableRows = table.getRows();

        assertThat(tableRows.size(), is(3));
        assertThat(tableRows.get(0), allOf(hasEntry("First Name", "Tim"),hasEntry("Last Name", "Brooke-Taylor"), hasEntry("Favorite Colour","Red")));
        assertThat(tableRows.get(1), allOf(hasEntry("First Name", "Graeme"),hasEntry("Last Name", "Garden"), hasEntry("Favorite Colour","Green")));
        assertThat(tableRows.get(2), allOf(hasEntry("First Name", "Bill"),hasEntry("Last Name", "Oddie"), hasEntry("Favorite Colour","Blue")));
    }

    @Test
    public void should_ignore_data_in_merged_cells() {
        HtmlTable table = new HtmlTable(page.table_with_merged_cells);

        List<Map<Object, String>> tableRows = table.getRows();

        assertThat(tableRows.size(), is(3));
        assertThat(tableRows.get(0), allOf(hasEntry("First Name", "Tim"),hasEntry("Last Name", "Brooke-Taylor"), hasEntry("Favorite Colour","Red")));
        assertThat(tableRows.get(1), allOf(hasEntry("First Name", "Graeme"),hasEntry("Last Name", "Garden"), hasEntry("Favorite Colour","Green")));
        assertThat(tableRows.get(2), allOf(hasEntry("First Name", "Bill"),hasEntry("Last Name", "Oddie"), hasEntry("Favorite Colour","Blue")));
    }

    @Test
    public void should_use_first_row_as_headers_if_no_th_cells_are_defined() {
        HtmlTable table = new HtmlTable(page.table_with_td_headers);

        List<Map<Object, String>> tableRows = table.getRows();

        assertThat(tableRows.size(), is(3));
        assertThat(tableRows.get(0), allOf(hasEntry("First Name", "Tim"),hasEntry("Last Name", "Brooke-Taylor"), hasEntry("Favorite Colour","Red")));
        assertThat(tableRows.get(1), allOf(hasEntry("First Name", "Graeme"),hasEntry("Last Name", "Garden"), hasEntry("Favorite Colour","Green")));
        assertThat(tableRows.get(2), allOf(hasEntry("First Name", "Bill"),hasEntry("Last Name", "Oddie"), hasEntry("Favorite Colour","Blue")));
    }
    @Test
    public void should_ignore_rows_with_missing_cells() {
        HtmlTable table = new HtmlTable(page.clients_with_missing_cells);

        List<Map<Object, String>> tableRows = table.getRows();

        assertThat(tableRows.size(), is(2));
        assertThat(tableRows.get(0), allOf(hasEntry("First Name", "Tim"),hasEntry("Last Name", "Brooke-Taylor"), hasEntry("Favorite Colour","Red")));
        assertThat(tableRows.get(1), allOf(hasEntry("First Name", "Bill"),hasEntry("Last Name", "Oddie"), hasEntry("Favorite Colour","Blue")));
    }

    @Test
    public void should_find_row_elements_matching_a_given_criteria() {
        List<WebElement> matchingRows = inTable(page.clients).getRowElementsWhere(the("First Name", is("Tim")), the("Last Name", containsString("Taylor")));
        assertThat(matchingRows.size(), is(1));
        assertThat(matchingRows.get(0).getText(), containsString("Brooke-Taylor"));
    }

    @Test
    public void should_find_row_elements_matching_a_given_criteria_using_a_static_method() {
        List<WebElement> matchingRows = HtmlTable.filterRows(page.clients, the("First Name", is("Tim")),the("Last Name", containsString("Taylor")));
        assertThat(matchingRows.size(), is(1));
        assertThat(matchingRows.get(0).getText(), containsString("Brooke-Taylor"));
    }

    @Test
    public void should_find_first_row_element_matching_a_given_criteria_using_a_static_method() {
        WebElement firstRow = inTable(page.clients).findFirstRowWhere(the("First Name", is("Tim")), the("Last Name", containsString("Taylor")));
        assertThat(firstRow.getText(), containsString("Brooke-Taylor"));
    }

    @Test(expected = AssertionError.class)
    public void should_throw_an_exception_if_no_matching_row_is_found() {
        inTable(page.clients).findFirstRowWhere(the("First Name", is("Unknown-name")));
    }

    @Test
    public void should_be_able_to_read_cells_with_empty_headers() {
        HtmlTable table = new HtmlTable(page.table_with_empty_headers);

        List<Map<Object, String>> tableRows = table.getRows();
        assertThat(tableRows.size(), is(3));
        assertThat(tableRows.get(0).get(4), is("Row 1, Cell 1"));
        assertThat(tableRows.get(0).get(5), is("Row 1, Cell 2"));
        assertThat(tableRows.get(1).get(4), is("Row 2, Cell 1"));
        assertThat(tableRows.get(1).get(5), is("Row 2, Cell 2"));
        assertThat(tableRows.get(2).get(4), is("Row 3, Cell 1"));
        assertThat(tableRows.get(2).get(5), is("Row 3, Cell 2"));
    }

}
