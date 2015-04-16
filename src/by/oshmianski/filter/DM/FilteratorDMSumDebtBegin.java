package by.oshmianski.filter.DM;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.Filterator;

import java.math.BigDecimal;
import java.util.List;

public class FilteratorDMSumDebtBegin implements Filterator<BigDecimal, DataMainItem> {
    @Override
    public void getFilterValues(List<BigDecimal> baseList, DataMainItem element) {

        baseList.add(element.getSumDebtBegin());
    }
}