package com.example.roboticsscoutingmatchapp;


import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class U extends AppCompatActivity{


    public final int DATA_VERSION = 1;
    // Characters to strip in input fields for stripText method
    public static final String [] DELIMITER = {",", ";", ":", "|", "\n"};
    public static final String [] WHITESPACE = {" "};
    public final String [] DELIMITER_AND_WHITESPACE = {",", ";", ":", "|", "\n", " "};


    // Returning data pulled from fields

    /**
     * Returns the two {@code Arrays} concatenated
     * @param a1 {@code Array}: the first {@code Array}
     * @param a2 {@code Array}: the second {@code Array}
     * @return An {@code Array} of type {@code Object[]} of the two {@code Arrays} concatenated
     * @author Akash Ghoshroy
     */
    public static Object[] concatArrays(Object[] a1, Object[] a2){
        int l = a1.length+a2.length;
        Object[] f = new Object[l];
        int i = 0;
        for(Object o : a1){
            f[i] = o;
            i++;
        }
        for(Object o : a2){
            f[i] = o;
            i++;
        }
        return f;
    }

    /**
     * @param field <code>EditText</code> component to pull String data from
     * @return A String with the text from the provided <code>EditText</code>
     * @author Akash Ghoshroy
     */
    public String getData(EditText field){
        return field.getText().toString(); // Literally just returns the String form of the text in the EditText
    }
    /**
     * @param field <code>RadioGroup</code> component to pull String data from
     * @return String with the text of the selected <code>RadioButton</code>
     * @author Akash Ghoshroy
     */
    public String getData(RadioGroup field){
        String response;
        if(field.getCheckedRadioButtonId() == -1){ // If no button is selected, return an empty String
            response = "";
        }else{
            RadioButton checkedButton = field.findViewById(field.getCheckedRadioButtonId()); // Get the currently selected button in the RadioGroup
            response = checkedButton.getText().toString(); // Return the text associated with the currently selected button in the RadioGroup
        }
        return response; // Returns either value
    }
    /**
     * @param field <code>TextView</code> component to pull String data from
     * @return String with the text of the provided <code>TextView</code>
     * @author Akash Ghoshroy
     */
    public String getData(TextView field){
        return field.getText().toString(); // Return the String form of the text in a TextView
    }
    /**
     * @param field <code>CheckBox</code> component to pull String data from.
     * @return String representing the <code>boolean</code> value of the <code>CheckBox</code>.
     * @author Akash Ghoshroy
     */
    public String getData(CheckBox field){
        if(field.isChecked()){ // Kinda self explanatory if you ask me (at akashghoshroytl@gmail.com)
            return "True";
        } else{
            return "False";
        }
    }

    /**
     * Removes all instances of each element in <code>stripType</code> from <code>text</code>.</br>
     * Can be used with the pre-defined String array constants {@link #DELIMITER}, {@link #WHITESPACE}, and {@link #DELIMITER_AND_WHITESPACE}.
     * @param text <code>String</code> that will be stripped of characters.
     * @param stripType Non-Null <code>String</code> array to be removed from <code>text</code>.
     * @see #DELIMITER
     * @see #WHITESPACE
     * @see #DELIMITER_AND_WHITESPACE
     * @return <code>text</code> sans all appearances of every element in <code>stripType</code>.
     * @author Akash Ghoshroy
     */
    public String stripText(String text,  String[] stripType){
        String response = text; // Sets the String to be returned to text
        for(String s : stripType){ // Loops through all elements of stripType
            while(response.contains(s)){ // Goes through every instance of each element of stripType and removes it
                response = response.substring(0, response.indexOf(s)) + response.substring(response.indexOf(s)+1);
            }
        }
        return response; // Returns text after it has been modified
    }
    /**
     * Removes all appearances of {@link #DELIMITER_AND_WHITESPACE} in <code>text</code>.
     * @param text <code>String</code> to be stripped.
     * @see #DELIMITER_AND_WHITESPACE
     * @return <code>text</code> sans all appearances of every element in {@link #DELIMITER_AND_WHITESPACE}.
     * @author Akash Ghoshroy
     */
    public String stripText(String text){
        String response = text; // Sets the String to be returned to text
        for(String s : DELIMITER_AND_WHITESPACE){ // Loops through all elements of DELIMITER_AND_WHITESPACE
            while(response.contains(s)){ // Goes through every instance of each element of stripType and removes it
                response = response.substring(0, response.indexOf(s)) + response.substring(response.indexOf(s)+1);
            }
        }
        return response; // Returns text after it has been modified
    }

    /**
     * Returns <code>text</code> from the first comma appearance and on, provided there is a comma.</br>
     * Otherwise, returns the entire <code>text</code>.
     * @param text <code>String</code>: a <code>String</code> to be returned from the next comma.
     * @return A copy of {@code text} from the next available comma on.
     * @author Akash Ghoshroy
     */
    public String nextCommaOn(String text){
        return text.substring(text.indexOf(",")+1);
    }

    /**
     * Returns <code>text</code> from the <code>commaNum</code>th comma on
     * @param text <code>String</code>: the {@code String} from which data should be returned
     * @param commaNum {@code int}: the {@code int} representing the comma from which data should be returned after
     * @return A {@code String} copy of {@code text} from the {@code commaNum}th comma on
     * @author Akash Ghoshroy
     */
    public String nextCommaOn(String text, int commaNum){
        if(commaNum <= 1){ return text.substring(text.indexOf(",")+1);}
        else{return nextCommaOn(text.substring(text.indexOf(",")+1), commaNum-1);}
    }

    /**
     * Returns a copy of {@code text} from the beginning of {@code text} to the next comma.</br>
     * Pre-Condition: {@code text} must contain at least one comma.
     * @param text {@code String}: a {@code String} for which a copy of until the next comma will be returned.
     * @return A copy of {@code text} from the beginning until the next comma.
     * @author Akash Ghoshroy
     */
    public String untilNextComma(String text){
        return text.substring(0, text.indexOf(","));
    }

    /**
     * Increments the value represented within {@code field} by one.</br>
     * Sets the value represented within {@code field} to one if {@code field} is empty.
     * @param field {@code TextView}: a {@code TextView} with either no value or an integer value.
     * @author Akash Ghoshroy
     */
    public void incrementText(TextView field){
        if(field.getText().toString().isEmpty()){
            field.setText("1"); // If the TextView is empty, it gets set to have a value of one
        }else{
            int currentNum = Integer.parseInt(field.getText().toString()); // Sets currentNum to the integer value within field
            currentNum++; // Increments currentNum
            field.setText(Integer.toString(currentNum)); // Sets the display value of field to currentNum
        }
    }

    /**
     * Increments the value represented within {@code field} by the value within {@code incrementBy}.</br>
     * Sets the value represented within {@code field} to the value of {@code incrementBy} if {@code field} is empty.
     * @param field {@code TextView}: a {@code TextView} with either no value or an integer value.
     * @param incrementBy {@code int}: an {@code int} with either a positive or negative value
     * @author Akash Ghoshroy
     */
    public void incrementText(TextView field, int incrementBy){
        if(field.getText().toString().isEmpty()){
            field.setText(Integer.toString(incrementBy)); // If the TextView is empty, it gets set to have the value of incrementBy
        }else{
            field.setText(Integer.toString(Integer.parseInt(field.getText().toString())+incrementBy)); // Sets the display value to the current display value + incrementBy
        }
    }

    /**
     * Increments the value represented within {@code field} by one.</br>
     * Sets the value represented within {@code field} to one if {@code field} is empty.
     * @param field {@code EditText}: an {@code EditText} with either no value or an integer value.
     * @author Akash Ghoshroy
     */
    public void incrementText(EditText field){
        if(field.getText().toString().isEmpty()){
            field.setText("1"); // If the EditText is empty, it gets set to have a value of one
        }else{
            int currentNum = Integer.parseInt(field.getText().toString()); // Sets currentNum to the integer value within field
            field.setText(Integer.toString(currentNum+1)); // Sets the display value to currentNum + one
        }
    }

    /**
     * Increments the value represented within {@code field} by the value within {@code incrementBy}.</br>
     * Sets the value represented within {@code field} to the value of {@code incrementBy} if {@code field} is empty.
     * @param field {@code EditText}: an {@code EditText} with either no value or an integer value.
     * @param incrementBy {@code int}: an {@code int} with either a positive or negative value
     * @author Akash Ghoshroy
     */
    public void incrementText(EditText field, int incrementBy){
        if(field.getText().toString().isEmpty()){
            field.setText(Integer.toString(incrementBy)); // If the EditText is empty, it gets set to have the value of incrementBy
        }else{
            int currentNum = Integer.parseInt(field.getText().toString()); // Sets currentNum to the integer value within field
            field.setText(Integer.toString(currentNum+incrementBy)); // Sets the display value to currentNum + incrementBy
        }
    }

    public int searchArrCol(ArrayList<Object[]> arr, int col, Object obj){
        int i = 0;
        for(Object[] o: arr){
            if(o[col].equals(obj)){return i;}
            i++;
        }
        return -1;
    }
}
