package org.smvc.test;

public class HelloWorldServiceImpl implements HelloWorldService {

    private String text;

    private OutputService outputService;

    public void helloWorld(){
        outputService.output(text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOutputService(OutputService outputService) {
        this.outputService = outputService;
    }

    public String getText() {
        return text;
    }

    public OutputService getOutputService() {
        return outputService;
    }
    
    

}
