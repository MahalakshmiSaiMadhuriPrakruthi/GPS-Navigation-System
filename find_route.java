import java.io.*;
import java.util.*;

class node implements Comparable<node> {
//  Declaration of the properties of the nodes
    node parent_node;
    int m_depth;
    float t_stepcost;
    String current_state;
    node t_childnode;
    public node(node parent_node, int m_depth, float t_stepcost, String current_state)
//  Constructor to initialize the nodes
    {
        this.parent_node = parent_node;
        this.m_depth = m_depth;
        this.t_stepcost = t_stepcost;
        this.current_state = current_state;
    }
    @Override
    public int compareTo(node arg0) 
    {
//  Fringe sorting
    return Float.compare(this.t_stepcost, arg0.t_stepcost);
    }
}

public class find_route {
    static int nodes_generated = 0, nodes_popped = 0, nodes_expanded = 0;
    static node goal_node=null;
    public static void main(String[ ] args ) 
    {
        String inputNodes_file = args[0]; //String declaration for the input file
        String src_node = args[1];
        String dest_node = args[2];
        ArrayList<String> array=new ArrayList<String>();  //Creating array list
        try {
            //To store the input of a the given text file.
            FileReader file = new FileReader(inputNodes_file);
            BufferedReader buffer =new BufferedReader(file);
            String line;
            while((line = buffer.readLine().toString()) .equals("END OF INPUT")==false) {
                array.add(line);
            }
            buffer.close();
        }
        catch(FileNotFoundException err) 
        {
        System.out.println("File cannot be opened");
        } 
        catch (IOException er) 
        {}

//      Initializing the values of root node, depth value, step cost and the source city 
        node parent_node = new node(null,0,0,src_node);

//      Incrementing the counter as the root node is generated        
        nodes_generated++;

//      Array list to store the fringe
        ArrayList<node> fringeArray=new ArrayList<node>();
        fringeArray.add(parent_node);

//      Array list to store the closed set of nodes        
        ArrayList<String> closed_array=new ArrayList<String>();

//      Popping the first element of the fringe and adding another at the end simultaneously
        while(!(fringeArray.isEmpty())&& goal_node==null) {
            node current_node  = fringeArray.remove(0);
            nodes_popped++;     
            if (current_node.current_state.equals(dest_node))
            {
                goal_node=current_node;
            }
            else{
                if (closed_array.contains(current_node.current_state)) {}
                else 
                {
                    closed_array.add (current_node.current_state);
                    nodes_expanded++;
                    for(String temp : array)
                    {
                        if(temp.contains(current_node.current_state)) {
                            nodes_generated++;
                            StringTokenizer token=new StringTokenizer(temp, " ");
                            String first_string=token.nextToken();
                            String second_string=token.nextToken();
                            float cost=(float)Integer.parseInt(token.nextToken());
                            if(current_node.current_state.equals(first_string))
                            {
                                node city_node = new node(current_node,current_node.m_depth+1,current_node.t_stepcost+cost,second_string);
                                fringeArray.add(city_node);
//                              Addition of successor node to the fringe
                            }
                            else 
                            {
                                  node city_node = new node(current_node,current_node.m_depth+1,current_node.t_stepcost+cost,first_string);
                                  fringeArray.add(city_node);
                            }
                        }
                    }
//                  Fringe sorting to get optimal solution
                    Collections.sort(fringeArray);
                }
            }
        }
 
 //     If there is no route that exists.
        if(goal_node==null) 
        {
        print_details();
        System.out.println("Distance:Infinity \n Route\n None");
        }
        else 
        update_goalNode();            
    }

    // Printing the final details of the search
    public static void print_details()
    {
        System.out.println("Nodes Popped : "+nodes_popped+"\nNodes Expanded : "+nodes_expanded+"\nNodes Generated : "+nodes_generated);
        if(goal_node!=null)
        System.out.println("Distance : "+goal_node.t_stepcost+" km"+"\nRoute : ");
    }

    public static void update_goalNode()
    {
        goal_node.t_childnode=null;
        find_route.print_details();
        while(goal_node.parent_node!=null) 
            {
                goal_node.parent_node.t_childnode=goal_node;
                goal_node=goal_node.parent_node;
            }
        while(goal_node.t_childnode!=null) 
            {
                System.out.println(goal_node.current_state+" to "+goal_node.t_childnode.current_state+" "+(goal_node.t_childnode.t_stepcost-goal_node.t_stepcost)+" km");
                goal_node=goal_node.t_childnode;
            }
    }
}