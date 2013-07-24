package mods.quantumcraft.net;

import java.util.ArrayList;
import java.util.List;

import mods.quantumcraft.net.RecursiveScanner.IDataGatherer;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class QuantumEnergyNet {
	private static class SourcePropagationDG implements IDataGatherer{
		private Location source;
		
		public SourcePropagationDG(Location src){
			this.source = src;
		}
		
		@Override
		public void gatherDataOnTile(World w, Location l){
			int id = w.getBlockId(l.getXCoord(), l.getYCoord(), l.getZCoord());
			Block b = Block.blocksList[id];
			if(b instanceof IQEnergySink){
				((IQEnergySink)b).addSourceToList(w, l, source);
			}
		}
	}
	
	private static class SourceScanningDG implements IDataGatherer{
		private List<Location> sources = new ArrayList<Location>();
		
		@Override
		public void gatherDataOnTile(World w, Location l) {
			int id = w.getBlockId(l.getXCoord(), l.getYCoord(), l.getZCoord());
			Block b = Block.blocksList[id];
			if(b instanceof IQEnergySource){
				sources.add(l);
			}
		}
		
		public List<Location> getList(){
			return sources;
		}
	}
	
	private static class BrokenLinkDG implements IDataGatherer{
		private List<Location> sources = new ArrayList<Location>();
		private List<Location> sinks = new ArrayList<Location>();
		
		@Override
		public void gatherDataOnTile(World w, Location l) {
			int id = w.getBlockId(l.getXCoord(), l.getYCoord(), l.getZCoord());
			Block b = Block.blocksList[id];
			if(b instanceof IQEnergySource){
				sources.add(l);
			}
			if(b instanceof IQEnergySink){
				sinks.add(l);
			}
		}
		
		public void addAllSourcesToAllSinks(World w){
			for(Location sink : sinks){
				int id = w.getBlockId(sink.getXCoord(), sink.getYCoord(), sink.getZCoord());
				Block b = Block.blocksList[id];
				if(b instanceof IQEnergySink){
					((IQEnergySink)b).replaceSourceList(w, sink, new EnergySourceList(sources));
				}
			}
		}
	}
	
	public static void propagateSourceLocation(World w, Location l){
		SourcePropagationDG dataGatherer = new SourcePropagationDG(l);
		RecursiveScanner.scan(w, l, dataGatherer);
	}
	
	public static List<Location> getSourceLocations(World w, Location l){
		SourceScanningDG dataGatherer = new SourceScanningDG();
		RecursiveScanner.scan(w, l, dataGatherer);
		return dataGatherer.getList();
	}
	
	public static void onBrokenLink(World w, Location[] tips){
		List<Location> memory = new ArrayList<Location>();
		for(Location tip : tips){
			BrokenLinkDG dataGatherer = new BrokenLinkDG();
			RecursiveScanner.scan(w, tip, dataGatherer, memory);
			dataGatherer.addAllSourcesToAllSinks(w);
		}
	}
}