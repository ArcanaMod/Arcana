package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;

import java.util.ArrayList;
import java.util.List;

/*
    This class is totally useless
    Luna have you better idea what to do with Spell Costs?
*/
public class SpellCosts {
    public AspectStack air;
    public AspectStack water;
    public AspectStack fire;
    public AspectStack earth;
    public AspectStack order;
    public AspectStack chaos;
    public int special;

    public SpellCosts(int air, int water, int fire, int earth, int order, int chaos, int special){
        this.air = new AspectStack(Aspects.AIR,air);
        this.water = new AspectStack(Aspects.WATER,water);
        this.fire = new AspectStack(Aspects.FIRE,fire);
        this.earth = new AspectStack(Aspects.EARTH,earth);
        this.order = new AspectStack(Aspects.ORDER,order);
        this.chaos = new AspectStack(Aspects.CHAOS,chaos);
        this.special = special;
    }

    public List<AspectStack> toList(){
        List<AspectStack> list = new ArrayList<>();
        if (air.getAmount() != 0)
        list.add(air);
        if (water.getAmount() != 0)
        list.add(water);
        if (fire.getAmount() != 0)
        list.add(fire);
        if (earth.getAmount() != 0)
        list.add(earth);
        if (order.getAmount() != 0)
        list.add(order);
        if (chaos.getAmount() != 0)
        list.add(chaos);
        return list;
    }
}
