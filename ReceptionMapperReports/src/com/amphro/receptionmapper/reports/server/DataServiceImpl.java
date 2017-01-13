package com.amphro.receptionmapper.reports.server;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.amphro.receptionmapper.reports.client.DataService;
import com.amphro.receptionmapper.reports.shared.Node;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService
{
	// Necessary in order to convert timestamp to comparable data
	//static Timestamp dataTime = new Timestamp(1);

	public Node[] getNodes()
	{
		
		String query = "SELECT * FROM Nodes LIMIT 10";
		List<Node> ret = new ArrayList<Node>();

		try
		{
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(query);
			
			while (result.next())
			{
				Node node = new Node(result.getFloat(Node.NODE_LATITUDE), result.getFloat(Node.NODE_LONGITUDE), result.getInt(Node.NODE_CLIENT), result.getString(Node.NODE_NETWORK), result
						.getInt(Node.NODE_SIGNAL_STRENGTH), result.getDate(Node.NODE_TIME));
				ret.add(node);
				System.out.println(node);
			}
			select.close();
			result.close();
			conn.close();
		} catch (SQLException e)
		{
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}

		Node[] n = new Node[ret.size()];

		for (int i = 0; i < ret.size(); i++)
		{
			n[i] = ret.get(i);
		}

		return n;
	}
}