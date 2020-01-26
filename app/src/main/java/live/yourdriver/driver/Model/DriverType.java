package live.yourdriver.driver.Model;

public class DriverType {

    String title;
    int imageResourceId;
    String expectedFare;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getExpectedFare() {
        return expectedFare;
    }

    public void setExpectedFare(String expectedFare) {
        this.expectedFare = expectedFare;
    }
}
