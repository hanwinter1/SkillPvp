package kr.hanwinter.skillpvp.game;

public enum GameLocation {
    LOBBY("로비")
    , PRACTICE("연습장")
    , SOLO_MATCH_WAITING("개인전대기")
    , SOLO_MATCH_LOCATION1_1("개인전위치1-1")
    , SOLO_MATCH_LOCATION1_2("개인전위치1-2")
    , SOLO_MATCH_LOCATION1_3("개인전위치1-3")
    , SOLO_MATCH_LOCATION1_4("개인전위치1-4")
    , SOLO_MATCH_LOCATION1_5("개인전위치1-5")
    , SOLO_MATCH_LOCATION1_6("개인전위치1-6")
    , SOLO_MATCH_LOCATION1_7("개인전위치1-7")
    , SOLO_MATCH_LOCATION1_8("개인전위치1-8")
    , SOLO_MATCH_LOCATION1_9("개인전위치1-9")
    , SOLO_MATCH_LOCATION1_10("개인전위치1-10")
    , TEAM_MATCH_WAITING("팀전대기");

    private final String displayName;

    GameLocation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
