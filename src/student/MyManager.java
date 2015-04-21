package student;

import java.util.*;

import game.*;

public class MyManager extends Manager{
	boolean basic = false;
	Node depot = null;
	public void run(){
		for(int i = 0; i < getTrucks().size(); i++)
			getTrucks().get(i).setUserData(new ArrayList<Parcel>());
		int k = 0;
		depot = getTrucks().get(k).getLocation();
		for(Parcel p : getParcels()){
			if(k == getTrucks().size())
				k = 0;
			((ArrayList<Parcel>)getTrucks().get(k).getUserData()).add(p);
			k++;
		}
		basic = true;
	}
	public void truckNotification(Truck t, Notification n){
		if(!basic)
			return;
		if(((ArrayList<Parcel>)t.getUserData()).size() == 0 && n == Notification.WAITING)
			t.setTravelPath(dijkstra(t.getLocation(), depot));
		else {
			if(n == Notification.WAITING && t.getLoad() == null)
				t.setTravelPath(dijkstra(t.getLocation(), 
					((ArrayList<Parcel>)t.getUserData()).get(((ArrayList<Parcel>)t.getUserData()).size()-1).getLocation()));
			else if (n == Notification.WAITING && t.getLoad() != null)
				t.setTravelPath(dijkstra(t.getLocation(), 
						((ArrayList<Parcel>)t.getUserData()).get(((ArrayList<Parcel>)t.getUserData()).size()-1).destination));
			if(t.getLocation() != null){
				if(((ArrayList<Parcel>)t.getUserData()).size() > 0 && t.getLoad() == null
					&& t.getLocation().equals(((ArrayList<Parcel>)t.getUserData()).get(((ArrayList<Parcel>)t.getUserData()).size()-1).start))
					t.pickupLoad(((ArrayList<Parcel>)t.getUserData()).get(((ArrayList<Parcel>)t.getUserData()).size()-1));
				if(((ArrayList<Parcel>)t.getUserData()).size() > 0 && t.getLoad() != null 
						&& t.getLocation().equals(((ArrayList<Parcel>)t.getUserData()).get(((ArrayList<Parcel>)t.getUserData()).size()-1).destination)){
					t.dropoffLoad();
					((ArrayList<Parcel>)t.getUserData()).remove(((ArrayList<Parcel>)t.getUserData()).size()-1);
				}
			}
		}
	}
	private List<Node> dijkstra(Node a, Node b){
		LinkedList<Node> path = new LinkedList<Node>();
		HashMap<Node, Integer> hm = new HashMap<Node, Integer>();
		HashMap<Node, Node> back = new HashMap<Node, Node>();
		for(Node o : getNodes())
			hm.put(o, Integer.MAX_VALUE);
		GriesHeap<Node> my = new GriesHeap<Node>();
		hm.remove(a);
		hm.put(a, 0);
		my.add(a, 0);
		back.put(a, null);
		while(!my.isEmpty()){
			Node x = my.poll();
			if(x.equals(b))
				break;
			for(Node e : x.getNeighbors().keySet()){
				Edge con = x.getConnect(e);
				if(hm.get(e) > hm.get(x) + con.length){
					hm.remove(e);
					hm.put(e, hm.get(x) + con.length);
					back.put(e, x);
					try{
						my.add(e, hm.get(e));
					}
					catch(IllegalArgumentException k){
						my.updatePriority(e, hm.get(e));
					}
				}
			}
		}
		Node curr = b;
		while(curr != null){
			path.add(curr);
			curr = back.get(curr);
		}
		Collections.reverse(path);
		return path;
	}
	public void sort(int[] b, int k) {
		PriorityQueue<Integer> heap = new PriorityQueue();
		for (int i = 0; i < k; i++) {
			heap.add(b[i]);
		}
		for (int i = 0; i < b.length - k; i++) {
			b[i] = heap.poll();
			heap.add(b[i + k]);
		}
		for (int i = b.length - k; i < b.length; i++) {
			b[i] = heap.poll();
		}
		for(int i = 0; i < b.length; i++)
			System.out.println(b[i]);
	}
}