package net.splurpy.paranoiamod.client;

public class ClientSanityData {

    private static int playerSanity;

    public static void set(int sanity) {
        ClientSanityData.playerSanity = sanity;
    }

    public static int getPlayerSanity() {
        return playerSanity;
    }
}
