package by.oshmianski.filter.DM;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ca.odell.glazedlists.Filterator;

import java.math.BigDecimal;
import java.util.List;

public class FilteratorDMDraft implements Filterator<Boolean, by.oshmianski.objects.DataMainItem> {
    @Override
    public void getFilterValues(List<Boolean> baseList, by.oshmianski.objects.DataMainItem element) {

        baseList.add(!element.isDraft());
    }
}