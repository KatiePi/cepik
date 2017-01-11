package utils;

/**
 * Created by Damian on 2017-01-10.
 */

import java.util.regex.*;

public class VinValidator {

    private byte VIN[] = new byte[17];
  //  private static final String REGEX = "^(([a-h,A-H,j-n,J-N,p-z,P-Z,0-9]{9})([a-h,A-H,j-n,J-N,p,P,r-t,R-T,v-z,V-Z,0-9])([a-h,A-H,j-n,J-N,p-z,P-Z,0-9])(d{6}))$";
  //  private static final String REGEX = "^(([a-h,A-H,j-n,J-N,p-z,P-Z,0-9]{9})([a-h,A-H,j-n,J-N,p,P,r-t,R-T,v-z,V-Z,0-9])([a-h,A-H,j-n,J-N,p-z,P-Z,0-9])(d{6}))$";

private static final String REGEX = "^(([a-h,A-H,j-n,J-N,p-z,P-Z,0-9]{9})([a-h,A-H,j-n,J-N,p,P,r-t,R-T,v-z,V-Z,0-9])([a-h,A-H,j-n,J-N,p-z,P-Z,0-9])(\\d{6}))$";
    private static Pattern pattern = Pattern.compile(REGEX);
    private static Matcher matcher;
    private boolean valid = false;


    public VinValidator(String VIN) {
            matcher = pattern.matcher(VIN);
            if (matcher.matches())
                valid = true;
            else valid = false;



    }


    public boolean isValid() {
        return valid;
    }

}
