package mx.edu.utng.wsmovie;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by UTNG on 30/03/2017.
 */

public class DetailExam implements KvmSerializable {

    private int id;
    private String cveMatter;
    private String cveContent;
    private String cveResult;
    private String cveQuestion;
    private String answer;
    private String examDepartament;
    private String reactive;

    public DetailExam(int id, String cveMatter, String cveContent, String cveResult, String cveQuestion, String answer, String examDepartament, String reactive) {
        this.id = id;
        this.cveMatter = cveMatter;
        this.cveContent = cveContent;
        this.cveResult = cveResult;
        this.cveQuestion = cveQuestion;
        this.answer = answer;
        this.examDepartament = examDepartament;
        this.reactive = reactive;
    }

    public DetailExam(){
        this(0, "", "", "", "", "", "", "");
    }

    @Override
    public Object getProperty(int i) {
        switch (i){
            case 0:
                return id;
            case 1:
                return cveMatter;
            case 2:
                return cveContent;
            case 3:
                return cveResult;
            case 4:
                return cveQuestion;
            case 5:
                return answer;
            case 6:
                return examDepartament;
            case 7:
                return reactive;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 8;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i){
            case 0:
                id = Integer.parseInt(o.toString());
                break;
            case 1:
                 cveMatter = o.toString();
            case 2:
                 cveContent = o.toString();
            case 3:
                 cveResult = o.toString();
            case 4:
                 cveQuestion = o.toString();
            case 5:
                 answer = o.toString();
            case 6:
                 examDepartament = o.toString();
            case 7:
                 reactive = o.toString();
            default:
                break;
        }

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name  ="id";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name  ="cveMatter";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name  ="cveContent";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name  ="cveResult";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name  ="cveQuestion";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name  ="answer";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name  ="examDepartament";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name  ="reactive";
                break;
            default:
                break;
        }
    }
}
