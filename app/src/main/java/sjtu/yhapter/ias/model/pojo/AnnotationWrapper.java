package sjtu.yhapter.ias.model.pojo;

import sjtu.yhapter.reader.model.pojo.Annotation;

public class AnnotationWrapper extends Annotation {
    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "AnnotationWrapper{" +
                super.toString() + '\'' +
                "feedback='" + feedback + '\'' +
                '}';
    }
}
