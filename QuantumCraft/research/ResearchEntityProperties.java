package mods.quantumcraft.research;

import net.minecraft.entity.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ResearchEntityProperties implements IExtendedEntityProperties {
	
	private boolean[] researchItems = new boolean[100];
	
	public ResearchEntityProperties() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean getUnlocked(int index)
	{
		return researchItems[index];
	}
	
	public ResearchEntityProperties setUnlocked(int index)
	{
		researchItems[index] = true;
		return this;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound = compound.getCompoundTag("QC_Research");
		for (int i = 0; i < researchItems.length; i++)
		{
			compound.setBoolean("i." + i , researchItems[i]);
		}
		compound.setInteger("cnt", researchItems.length);
		System.out.print("SAVING REP");
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		compound = compound.getCompoundTag("QC_Research");
		for (int i = 0; i < compound.getInteger("cnt"); i++)
		{
			researchItems[i] = compound.getBoolean("i." + i);
		}
		System.out.print("LOADING REP");
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub

	}

}
