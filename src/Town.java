/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean treasurecooldown;
    private boolean cheatMode;



    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param s The town's shoppe.
     * @param t The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean cheatMode)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        treasurecooldown = false;

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param h The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }
    public String searchForTreasure()
    {
        String treasure = null;

        if (!treasurecooldown)
        {
            int num = (int) (Math.random() * 4);
            if (num == 0)
            {
                treasure = "Ruby";
            }
            else if (num == 1)
            {
                treasure = "Emerald";
            }
            else if (num == 2)
            {
                treasure = "Diamond";
            }
            else
            {
                treasure = "";
            }
            treasurecooldown = true;
        }
        return treasure;
    }
    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble(boolean easyMode)
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            if (easyMode) {
                noTroubleChance -= 0.1;
            }
            printMessage = "You want trouble, stranger! "+getFightAnimation();
            int goldDiff = (int)(Math.random() * 10) + 1;

            if (cheatMode)
            {
                goldDiff = 100;
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else if (Math.random() > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
                if (easyMode) {
                    hunter.changeGold(2);
                }
            }
            else
            {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
                if (easyMode) {
                    hunter.changeGold(3);
                }
            }
        }
    }

    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "water");
        }
        else if (rnd <9) {
            return new Terrain("SkyIsland","glider");
        } else
        {
            return new Terrain("Jungle", "machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }
    /*
     * Returns different fight "animations" depending on the terrain
     */
    private String getFightAnimation()
    {
        String terrainName = terrain.getTerrainName();

        if (terrainName.equals("Mountains"))
        {
            return "Fwoooooosh! bam! bam! crack!\n";
        }
        else if (terrainName.equals("Ocean"))
        {
            return "Splish! Splash! boom! bam!\n";
        }
        else if (terrainName.equals("Plains"))
        {
            return "Shshsh! rattling! crack! pow! boom!\n";
        }
        else if (terrainName.equals("Desert"))
        {
            return "PstPst! Sandy! Windy! bam! \n";
        }
        else if (terrainName.equals("Jungle"))
        {
            return "ROAARRRRRR! eeeoo! ooo! oww! pop!\n";
        }
        else
        {
            return "Ssssshhhhh! Argh! Ugh! OOf! POW!\n";
        }
    }
}