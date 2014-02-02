package by.oshmianski.filter.DM;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ca.odell.glazedlists.Filterator;

import java.math.BigDecimal;
import java.util.List;

public class FilteratorDMSumSalary implements Filterator<BigDecimal, by.oshmianski.objects.DataMainItem> {
    @Override
    public void getFilterValues(List<BigDecimal> baseList, by.oshmianski.objects.DataMainItem element) {

        baseList.add(element.getSumSalary());
    }
}