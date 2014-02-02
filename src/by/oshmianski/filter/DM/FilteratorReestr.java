package by.oshmianski.filter.DM;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import by.oshmianski.objects.Reestr;
import ca.odell.glazedlists.TextFilterator;

import java.util.List;

public class FilteratorReestr implements TextFilterator<Reestr> {
    public void getFilterStrings(List<String> baseList, Reestr item) {

        baseList.add(item.getTitle());
    }
}