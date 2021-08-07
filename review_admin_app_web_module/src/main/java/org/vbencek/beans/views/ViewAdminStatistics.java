/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.EmployeeFacadeLocal;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Book;
import org.vbencek.model.Employee;
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminStatistics")
@ViewScoped
public class ViewAdminStatistics implements Serializable {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @EJB(beanName = "EmployeeFacade")
    EmployeeFacadeLocal employeeFacade;

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;
    
    @Inject
    Localization localization;
    ResourceBundle res;
    
    @Getter
    @Setter
    BarChartModel barHighestRatedBooks;

    @Getter
    @Setter
    HorizontalBarChartModel hbarPopularBooks;

    @Getter
    @Setter
    PieChartModel pieMostActiveUsers;
    
    @Getter
    @Setter
    PieChartModel pieMostActiveMods;

    @PostConstruct
    public void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        createHighestRatedBooksModel();
        createPopularBooksModel();
        createMostActiveUsersModel();
        createMostActiveModsModel();
    }

    private List<String> getMyColumnColors() {
        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        bgColor.add("rgba(0, 153, 227, 0.2)");
        bgColor.add("rgba(101, 103, 107, 0.2)");
        bgColor.add("rgba(101, 103, 207, 0.2)");
        return bgColor;
    }

    private List<String> getMyBorderColors() {
        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        borderColor.add("rgba(0, 153, 227, 0.2)");
        borderColor.add("rgba(101, 103, 107, 0.2)");
        borderColor.add("rgba(101, 103, 207, 0.2)");
        return borderColor;
    }

    public void createHighestRatedBooksModel() {
        barHighestRatedBooks = new BarChartModel();
        ChartData data = new ChartData();
        List<Book> popularBooks = bookFacade.findBooksByCriteria("", "", 0, "", 0, "rating", 0, 10);

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel(res.getString("admin.ViewAdminStatistics.barRating.title"));

        List<Number> averageRating = new ArrayList<>();
        List<String> bookTitles = new ArrayList<>();
        for (Book book : popularBooks) {
            averageRating.add(book.getAverageRating());
            bookTitles.add(book.getTitle());
        }

        barDataSet.setData(averageRating);

        barDataSet.setBackgroundColor(getMyColumnColors());
        barDataSet.setBorderColor(getMyBorderColors());
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        data.setLabels(bookTitles);
        barHighestRatedBooks.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText(res.getString("admin.ViewAdminStatistics.barRating.legend"));
        options.setTitle(title);

        barHighestRatedBooks.setOptions(options);
    }

    public void createPopularBooksModel() {
        hbarPopularBooks = new HorizontalBarChartModel();
        ChartData data = new ChartData();
        List<Book> popularBooks = bookFacade.findBooksByCriteria("", "", 0, "", 0, "reviews", 0, 10);

        HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
        hbarDataSet.setLabel(res.getString("admin.ViewAdminStatistics.hbarRatingCount.title"));

        List<Number> ratingCounts = new ArrayList<>();
        List<String> bookTitles = new ArrayList<>();
        for (Book book : popularBooks) {
            ratingCounts.add(book.getRatingsCount());
            bookTitles.add(book.getTitle());
        }

        hbarDataSet.setData(ratingCounts);

        hbarDataSet.setBackgroundColor(getMyColumnColors());
        hbarDataSet.setBorderColor(getMyBorderColors());
        hbarDataSet.setBorderWidth(1);

        data.addChartDataSet(hbarDataSet);

        data.setLabels(bookTitles);
        hbarPopularBooks.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addXAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText(res.getString("admin.ViewAdminStatistics.hbarRatingCount.legend"));
        options.setTitle(title);

        hbarPopularBooks.setOptions(options);
    }

    private List<String> getPieChartColors() {
        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(255, 205, 86)");
        bgColors.add("rgb(54, 55, 55)");
        bgColors.add("rgb(255, 205, 200)");
        return bgColors;
    }

    private void createMostActiveUsersModel() {
        pieMostActiveUsers = new PieChartModel();
        ChartData data = new ChartData();
        List<UserT> users = userTFacade.findAll();

        PieChartDataSet dataSet = new PieChartDataSet();

        List<Number> numActivites = new ArrayList<>();
        List<String> usernames = new ArrayList<>();

        for (UserT user : users) {
            if (user.getDataLogList().size() > 0) {
                numActivites.add(user.getDataLogList().size());
                usernames.add(user.getUsername());
            }
        }
        dataSet.setData(numActivites);
        dataSet.setBackgroundColor(getPieChartColors());
        data.addChartDataSet(dataSet);
        data.setLabels(usernames);
        pieMostActiveUsers.setData(data);

        PieChartOptions options = new PieChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText(res.getString("admin.ViewAdminStatistics.pieUsers.title"));
        options.setTitle(title);

        pieMostActiveUsers.setOptions(options);
    }
    
     private void createMostActiveModsModel() {
        pieMostActiveMods = new PieChartModel();
        ChartData data = new ChartData();
        List<Employee> employees = employeeFacade.findAll();

        PieChartDataSet dataSet = new PieChartDataSet();

        List<Number> numActivites = new ArrayList<>();
        List<String> usernames = new ArrayList<>();

        for (Employee employee : employees) {
            if (employee.getDataLogEmployeesList().size() > 0) {
                numActivites.add(employee.getDataLogEmployeesList().size());
                usernames.add(employee.getUsername());
            }
        }
        dataSet.setData(numActivites);
        dataSet.setBackgroundColor(getPieChartColors());
        data.addChartDataSet(dataSet);
        data.setLabels(usernames);
        pieMostActiveMods.setData(data);

        PieChartOptions options = new PieChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText(res.getString("admin.ViewAdminStatistics.pieMods.title"));
        options.setTitle(title);

        pieMostActiveMods.setOptions(options);
    }
}
