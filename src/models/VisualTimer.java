package models;

import eu.hansolo.medusa.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class VisualTimer {
    private Gauge bigGauge, smallGauge;
    private AnimationTimer timer;
    private int focusLength;
    private int breakLength;

    public VisualTimer(int focusLength, int breakLength){
        this.focusLength = focusLength;
        this.breakLength = breakLength;
    }


    public Pane getTimer(){

        bigGauge = GaugeBuilder.create()
                .foregroundBaseColor(Color.WHITE)
                .prefSize(360, 360)
                .startAngle(270)
                .angleRange(270)
                .minValue(0)
                .maxValue(focusLength + breakLength)
                .tickLabelLocation(TickLabelLocation.OUTSIDE)
                .tickLabelOrientation(TickLabelOrientation.ORTHOGONAL)
                .minorTickMarksVisible(false)
                .majorTickMarkType(TickMarkType.BOX)
                .valueVisible(false)
                .knobType(Gauge.KnobType.FLAT)
                .needleShape(Gauge.NeedleShape.FLAT)
                .needleColor(Color.WHITE)
                .sectionsVisible(true)
                .sections(new Section(0, focusLength, Color.BLUE),
                        new Section(focusLength, focusLength+breakLength, Color.GREEN))
                .animated(true)
                .build();

        smallGauge = GaugeBuilder.create()
                .prefSize(150, 150)
                .foregroundBaseColor(Color.WHITE)
                .minValue(0)
                .maxValue(60)
                .minorTickMarksVisible(false)
                .mediumTickMarkType(TickMarkType.DOT)
                .majorTickMarkType(TickMarkType.BOX)
                .tickLabelOrientation(TickLabelOrientation.ORTHOGONAL)
                .knobType(Gauge.KnobType.FLAT)
                .needleShape(Gauge.NeedleShape.FLAT)
                .needleColor(Color.WHITE)
                .valueVisible(false)
                .customTickLabelsEnabled(true)
                .customTickLabels("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
                .customTickLabelFontSize(30)
                .animated(true)
                .build();

        Pane pane = new Pane(bigGauge, smallGauge);
        pane.setBackground(new Background(new BackgroundFill(Gauge.DARK_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        bigGauge.relocate(400, 0);
        smallGauge.relocate(400, 230);

        return pane;
    }

    public double getBigGaugeValue(){
        return bigGauge.getValue();
    }

    public double getSmallGaugeValue(){
        return smallGauge.getValue();
    }

    public void setBigGauge(double value){
        this.bigGauge.setValue(value);
    }

    public void setSmallGauge(double value){
        this.smallGauge.setValue(value);
    }

}
