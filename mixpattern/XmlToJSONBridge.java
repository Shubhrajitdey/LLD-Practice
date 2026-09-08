package mixpattern;

interface JSONData{
    public String getData();
}
class JSONDataSource implements JSONData{
    public String getData(){
        return "JSON DataSource it is";
    }
}

class XMLDataSource{
    public String getXMLData(){
        return ("Actual XMLL Data Source it is");
    }
}

class XMLDataSourceAdapter implements JSONData{
    private XMLDataSource xmlDataSource = new XMLDataSource();
    public String getData(){
        return xmlDataSource.getXMLData();
    }
}

class ReportingEngine{
    private JSONData jsonData;

    public ReportingEngine(JSONData jsonData) {
        this.jsonData = jsonData;
    }

    public void executedType(){
        System.out.println(jsonData.getData());
    }
    
}

public class XmlToJSONBridge {
    public static void main(String[] args) {
        ReportingEngine reportingJSONEngine = new ReportingEngine(new JSONDataSource());
        reportingJSONEngine.executedType();
        ReportingEngine reportingXMLEngine = new ReportingEngine(new XMLDataSourceAdapter());
        reportingXMLEngine.executedType();
    }
}
