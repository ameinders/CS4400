import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Driver {
	private static final String APP_TITLE = "Food Pantry";
	private static final int APP_WIDTH = 600, APP_HEIGHT = 600;

	private static JFrame applicationFrame = null;
	private static JPanel activePanel = null;

	private static LoginPanel login_panel = null;
	private static HomePanel home_panel = null;
	private static PickupsPanel pickups_panel = null;
	private static FamilyBagPickupPanel family_bag_pickup_panel = null;
	private static DropOffPanel drop_offs_panel = null;
	private static SearchClientPanel search_client_panel = null;
	private static NewClientPanel new_client_panel = null;
	private static AddFamilyPanel add_family_panel = null;
	private static BagListPanel bag_list_panel = null;
	private static EditBagPanel edit_bag_panel = null;
	private static ProductListPanel product_list_panel = null;
	private static NewProductPanel new_product_panel = null;
	private static MSRPanel msr_panel = null;
	private static GroceryPanel grocery_list_panel = null;

	public static void main(String[] args) {
		/* Create JFrame for application */
		applicationFrame = new JFrame(APP_TITLE);
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationFrame.setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));

		login_panel = new LoginPanel(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				loginAttempt(login_panel.getLoginInfo().getUsername(),
						login_panel.getLoginInfo().getPassword());
			}
		});

		setPanel(login_panel);
		applicationFrame.setVisible(true);
	}

	private static void loginAttempt(String username, String password) {
		Database db = new Database();
		System.out.printf(
				"User attempted login with username %s and password %s.\n",
				username, password);
		//boolean success = true;
		if (db.login(username, password)) {
			setPanel(getHomePanel());
		} else {
			JOptionPane.showMessageDialog(applicationFrame,
					"Invalid username or password!", "Login Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private static HomePanel getHomePanel() {
		if (home_panel == null) {
			ActionListener pickupsListener = new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setPanel(getPickupsPanel());
				}
			};
			ActionListener bagsListener = new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						setPanel(getBagListPanel());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			ActionListener dropoffsListener = new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setPanel(getDropOffPanel());
				}
			};
			ActionListener clientsListener = new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setPanel(getSearchClientPanel());
				}
			};
			ActionListener serviceReportListener = new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setPanel(getMSRPanel());
				}
			};
			ActionListener productsListener = new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setPanel(getProductListPanel());
				}
			};
			home_panel = new HomePanel(pickupsListener, bagsListener,
					dropoffsListener, clientsListener, serviceReportListener,
					productsListener);
		}
		return home_panel;
	}

	private static PickupsPanel getPickupsPanel() {
		Database db = new Database();
		if (pickups_panel == null) {
			pickups_panel = new PickupsPanel(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					setPanel(getHomePanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					System.out
							.println("User selected from client search table with ID "
									+ ae.getID() + ".");
					int clientID = ae.getID();
					setPanel(getFamilyBagPanel(clientID));
				}
			});
		}
		return pickups_panel;
	}

	private static FamilyBagPickupPanel getFamilyBagPanel(int clientID) {
		if (family_bag_pickup_panel == null) {
			family_bag_pickup_panel = new FamilyBagPickupPanel(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							System.out.println("Complete this pickup!");
							setPanel(getPickupsPanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							setPanel(getPickupsPanel());
						}
					}, clientID);
		}
		return family_bag_pickup_panel;
	}

	private static DropOffPanel getDropOffPanel() {
		if (drop_offs_panel == null) {
			drop_offs_panel = new DropOffPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getHomePanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Complete Drop Off");
					setPanel(getHomePanel());
				}
			});
		}
		return drop_offs_panel;
	}

	private static SearchClientPanel getSearchClientPanel() {
		if (search_client_panel == null) {
			search_client_panel = new SearchClientPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getNewClientPanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getAddFamilyPanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getHomePanel());
				}
			});
		}
		return search_client_panel;
	}

	private static NewClientPanel getNewClientPanel() {
		if (new_client_panel == null) {
			new_client_panel = new NewClientPanel(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("Save a new client plox");
					setPanel(getSearchClientPanel());
				}
			});
		}
		return new_client_panel;
	}

	private static AddFamilyPanel getAddFamilyPanel() {
		if (add_family_panel == null) {
			add_family_panel = new AddFamilyPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("We gotta SAVE THAT FAMILY NAO");
					setPanel(getSearchClientPanel());
				}
			});
		}
		return add_family_panel;
	}

	private static BagListPanel getBagListPanel() throws SQLException {
		if (bag_list_panel == null) {
			bag_list_panel = new BagListPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Edit bag please");
					setPanel(getEditBagPanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getHomePanel());
				}
			});
		}
		return bag_list_panel;
	}

	private static EditBagPanel getEditBagPanel() {
		if (edit_bag_panel == null) {
			edit_bag_panel = new EditBagPanel(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						setPanel(getBagListPanel());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Save bag!!!!");
					try {
						setPanel(getBagListPanel());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return edit_bag_panel;
	}

	private static ProductListPanel getProductListPanel() {
		if (product_list_panel == null) {
			product_list_panel = new ProductListPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getHomePanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getNewProductPanel());
				}
			});
		}
		return product_list_panel;
	}

	private static NewProductPanel getNewProductPanel() {
		if (new_product_panel == null) {
			new_product_panel = new NewProductPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getProductListPanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Save product plz");
					setPanel(getProductListPanel());
				}
			});
		}
		return new_product_panel;
	}

	private static MSRPanel getMSRPanel() {
		if (msr_panel == null) {
			msr_panel = new MSRPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getHomePanel());
				}
			}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getGroceryPanel());
				}
			});
		}
		return msr_panel;
	}

	private static GroceryPanel getGroceryPanel() {
		if (grocery_list_panel == null) {
			grocery_list_panel = new GroceryPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPanel(getMSRPanel());
				}
			});
		}
		return grocery_list_panel;
	}

	private static void setPanel(JPanel panel) {
		if (activePanel != null)
			applicationFrame.remove(activePanel);
		applicationFrame.add(panel);
		activePanel = panel;
		applicationFrame.pack();
		applicationFrame.invalidate();
		applicationFrame.validate();
		applicationFrame.repaint();
	}
}
