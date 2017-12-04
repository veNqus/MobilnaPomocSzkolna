package com.example.kacperp.zarzadzaniezadaniami;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by KacperP on 21.11.2017.
 */

public class Zadania implements Serializable {
    public int Id;
    public String Temat;
    public String Informacje;
    public String NazwaZajec;
    public Date Termin;
    public Date DataDodania;
    public boolean CzyZrobione  = false;
    public String IdUzytkownika;
}
