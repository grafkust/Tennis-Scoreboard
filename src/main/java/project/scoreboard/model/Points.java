package project.scoreboard.model;

import lombok.Getter;

@Getter
public enum Points {
   POINTS_EMPTY(""),
   POINTS_0("0"),
   POINTS_15("15"),
   POINTS_30("30"),
   POINTS_40("40"),
   POINTS_AD("AD");

   private final String points;

   Points(String points){
       this.points = points;
   }


}
