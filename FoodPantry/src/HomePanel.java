import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HomePanel extends JPanel {
	private JButton pickupsButton, bagsButton, dropoffsButton, clientsButton, serviceReportButton, productsButton;

	public HomePanel(ActionListener pickupListener, ActionListener bagsListener, ActionListener dropoffsListener, ActionListener clientsListner, ActionListener serviceReportListener, ActionListener productsListener) {
		super(new GridBagLayout());
		Image pickups_icon = null, bags_icon = null, dropoffs_icon = null, clients_icon = null, serviceReport_icon = null, products_icon = null;
		try {
			pickups_icon = ImageIO.read(new File("src/Home_Pickups.png"));
			bags_icon = ImageIO.read(new File("src/Home_Bags.png"));
			dropoffs_icon = ImageIO.read(new File("src/Home_DropOffs.png"));
			clients_icon = ImageIO.read(new File("src/Home_Clients.png"));
			serviceReport_icon = ImageIO.read(new File("src/Home_ServiceReport.png"));
			products_icon = ImageIO.read(new File("src/Home_Products.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		pickupsButton = new JButton(new ImageIcon(pickups_icon));
		pickupsButton.addActionListener(pickupListener);
		bagsButton = new JButton(new ImageIcon(bags_icon));
		bagsButton.addActionListener(bagsListener);
		dropoffsButton = new JButton(new ImageIcon(dropoffs_icon));
		dropoffsButton.addActionListener(dropoffsListener);
		clientsButton = new JButton(new ImageIcon(clients_icon));
		clientsButton.addActionListener(clientsListner);
		serviceReportButton = new JButton(new ImageIcon(serviceReport_icon));
		serviceReportButton.addActionListener(serviceReportListener);
		productsButton = new JButton(new ImageIcon(products_icon));
		productsButton.addActionListener(productsListener);

		JPanel contentPanel = new JPanel(new BorderLayout());
		JPanel homePanel = new JPanel(new GridLayout(2, 3) {
			{
				setVgap(5);
				setHgap(5);
			}
		});
		homePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		homePanel.add(pickupsButton);
		homePanel.add(bagsButton);
		homePanel.add(dropoffsButton);
		homePanel.add(clientsButton);
		homePanel.add(serviceReportButton);
		homePanel.add(productsButton);
		contentPanel.add(homePanel);
		add(contentPanel);
	}
}
