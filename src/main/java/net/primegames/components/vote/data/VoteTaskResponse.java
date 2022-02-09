package net.primegames.components.vote.data;

import lombok.Getter;

public class VoteTaskResponse {

    @Getter
    public boolean voted;
    @Getter
    public boolean claimed;
    @Getter
    public String type;

    public VoteTaskResponse(boolean voted, boolean claimed, String type) {
        this.voted = voted;
        this.claimed = claimed;
        this.type = type;
    }
}
