package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;
import org.vbencek.beans.requests.GenFunctions;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.ReviewFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Book;

/**
 * Class that generates statistic for single book
 * @author vbencek
 */
@Named(value = "viewBookStatistic")
@ViewScoped
public class ViewBookStatistic implements Serializable {
    
    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;
    
    @EJB(beanName = "ReviewFacade")
    ReviewFacadeLocal reviewFacade;
    
    @Inject
    GenFunctions genFunctions;
    
    @Inject
    Localization localization;
    ResourceBundle res;
    
    @PostConstruct
    public void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
    }
    
    /**
     * method calculates differences between thos number.
     * Returns percantage.
     * @return 
     */
    private double calculateDifferenceInPerc(double first, double second){
        double diff=first-second;
        double diff2=diff/second;
        double perc=diff2*100;
        return perc;
    }
    
    public String showAverageRatingComparison(Book book){
        double allBooksRating=bookFacade.getAverageRatingAllBooks();
        double thisRating=book.getAverageRating();
        double difference=calculateDifferenceInPerc(thisRating,allBooksRating);
        String strPerc=genFunctions.roundDouble(abs(difference))+"%";
        
        //checks if returned percantage value is negative and sets text accordingly
        String isNegative=res.getString("ViewBookStatistic.m1.more");
        if(String.valueOf(difference).contains("-")){
            isNegative=res.getString("ViewBookStatistic.m1.less");
        } 
        return res.getString("ViewBookStatistic.m1.1")+" "+genFunctions.roundDouble(thisRating)+" "+
               res.getString("ViewBookStatistic.m1.2")+" "+strPerc+" "+isNegative+" "+
               res.getString("ViewBookStatistic.m1.3")+" "+genFunctions.roundDouble(allBooksRating)+".";
    }
    
    public String showAverageCountComparison(Book book){
        double allBooksRatingsCount=bookFacade.getAverageRatingCountAllBooks();
        long thisRatingsCount=book.getRatingsCount();
        double difference=calculateDifferenceInPerc((double)thisRatingsCount,allBooksRatingsCount);
        String strPerc=genFunctions.roundDouble(abs(difference))+"%";
        
        //checks if returned percantage value is negative and sets text accordingly
        String isNegative=res.getString("ViewBookStatistic.m2.more");
        if(String.valueOf(difference).contains("-")){
            isNegative=res.getString("ViewBookStatistic.m2.less");
        } 
        return res.getString("ViewBookStatistic.m2.1")+" "+thisRatingsCount+" "+ res.getString("ViewBookStatistic.m2.2")+" "+
                res.getString("ViewBookStatistic.m2.3")+" "+strPerc+" "+isNegative+" "+
                res.getString("ViewBookStatistic.m2.4")+" "+genFunctions.roundDouble(allBooksRatingsCount)+".";
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
    
    public PieChartModel createBookReviewsPieModel(Book book){
        PieChartModel pieBookReviews=new PieChartModel();

        ChartData data = new ChartData();
        PieChartDataSet dataSet = new PieChartDataSet();

        List<Number> ratingNumber = new ArrayList<>();
        ratingNumber.add(reviewFacade.countRatingScore(book, 1));
        ratingNumber.add(reviewFacade.countRatingScore(book, 2));
        ratingNumber.add(reviewFacade.countRatingScore(book, 3));
        ratingNumber.add(reviewFacade.countRatingScore(book, 4));
        ratingNumber.add(reviewFacade.countRatingScore(book, 5));
        List<String> ratingValue = new ArrayList<>();
        ratingValue.add(res.getString("combobox.rating.one"));
        ratingValue.add(res.getString("combobox.rating.two"));
        ratingValue.add(res.getString("combobox.rating.three"));
        ratingValue.add(res.getString("combobox.rating.four"));
        ratingValue.add(res.getString("combobox.rating.five"));

        dataSet.setData(ratingNumber);
        dataSet.setBackgroundColor(getPieChartColors());
        data.addChartDataSet(dataSet);
        data.setLabels(ratingValue);
        pieBookReviews.setData(data);

        PieChartOptions options = new PieChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText(res.getString("ViewBookStatistic.pie.title"));
        options.setTitle(title);

        pieBookReviews.setOptions(options);
        
        return pieBookReviews;
    }
    
}
