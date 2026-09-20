package mixpattern;

import java.util.ArrayList;
import java.util.List;

class SpreedSheet implements Cloneable{
    private final List<CellStyle> cellList = new ArrayList<>();
    public void modifycell(String color, int start, int end){
        CellStyle cellStyle = new CellStyle(color,start,end);
        cellList.add(cellStyle);
    }
    public void display(){
        for(CellStyle cell:cellList){
            cell.show();
        }
    }
    @Override
    public SpreedSheet clone() {
        try{
            return (SpreedSheet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supportedd", e); // Should never happen
        }
    }
}

class CellStyle{
    private final String color;
    private final int start;
    private final int end;
    public CellStyle(String color, int start, int end){
        this.color = color;
        this.start = start;
        this.end = end;
    }
    public void show(){
        System.out.println("Cell looks like color:" + color + " start : "+ start + " end :" + end);
    }
}

public class SpreadsheetClone {
    public static void main(String[] args) {
        SpreedSheet sheet1 = new SpreedSheet();
        sheet1.modifycell("Red", 10, 45);
        System.out.println("--- Sheet 1 ---");
        sheet1.display();
        SpreedSheet sheet2 = sheet1.clone();
        System.out.println("--- Sheet 2 ---");
        sheet2.display();

        sheet2.modifycell("Blue", 20, 45);

        System.out.println("--- Sheet 1 after sheet 2 modified---");
        sheet1.display(); 

        System.out.println("--- Sheet 2 after sheet 2 modified ---");
        sheet2.display();
    }
}
