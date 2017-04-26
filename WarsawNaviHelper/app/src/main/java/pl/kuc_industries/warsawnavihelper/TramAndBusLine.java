package pl.kuc_industries.warsawnavihelper;

/**
 * Created by mateusz on 4/25/17.
 */

class TramAndBusLine {

    private String lineName;
    private boolean visible;

    public TramAndBusLine(String lineName) {
        this.lineName = lineName;
        this.visible = false;
    }

    public String getLineName() {
        return lineName;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
