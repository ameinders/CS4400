import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Driver {
	private static final String APP_TITLE = "Food Pantry";
	private static final int APP_WIDTH = 600, APP_HEIGHT = 600;

	private static String username = "";

	private static Database db = null;

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
		db = new Database();
		applicationFrame = new JFrame(APP_TITLE);
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationFrame.setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
		setPanel(getLoginPanel());
		applicationFrame.setVisible(true);
	}

	private static void setUser(String username) {
		Driver.username = username;
	}

	private static LoginPanel getLoginPanel() {
		if (login_panel == null) {
			login_panel = new LoginPanel(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String username = login_panel.getLoginInfo().getUsername();
					String password = login_panel.getLoginInfo().getPassword();
					System.out.printf("User attempted login with username %s and password %s.\n", username, password);
					if (db.login(username, password)) {
						setUser(username);
						setPanel(getHomePanel());
					} else {
						JOptionPane.showMessageDialog(applicationFrame, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return login_panel;
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
					setPanel(getBagListPanel());
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
			home_panel = new HomePanel(pickupsListener, bagsListener, dropoffsListener, clientsListener, serviceReportListener, productsListener);
		}
		return home_panel;
	}

	private static PickupsPanel getPickupsPanel() { // TODO: Buttons
		if (pickups_panel == null) {
			pickups_panel = new PickupsPanel(new ActionListener() { // Return button
						public void actionPerformed(ActionEvent ae) {
							setPanel(getHomePanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent ae) { // Sign-in button
							System.out.println("User selected from client search table with ID " + ae.getID() + ".");
							int clientID = ae.getID();
							setPanel(getFamilyBagPanel(clientID, ae.getActionCommand()));
						}
					}, new ActionListener() { // Day select
						public void actionPerformed(ActionEvent e) {
							System.out.println("User has requested pickup data.");
							ResultSet rs = db.viewPickups(pickups_panel.getSearchDay());
							if (rs != null) {
								pickups_panel.setData(rs);
							} else {
								showError("Database Error", "Failed to load pickup data");
							}
						}
					});
			ResultSet rs = db.viewPickups(pickups_panel.getSearchDay());
			if (rs != null) {
				pickups_panel.setData(rs);
			} else {
				showError("Database Error", "Failed to load pickup data");
			}
		}
		return pickups_panel;
	}

	private static FamilyBagPickupPanel getFamilyBagPanel(int clientID, String clientName) {
		System.out.println("Loading family bag panel for client with ID " + clientID);
		family_bag_pickup_panel = new FamilyBagPickupPanel(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				db.confirmPickup(family_bag_pickup_panel.getCID());
				setPanel(getPickupsPanel());
			}
		}, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPanel(getPickupsPanel());
			}
		}, clientID);
		ResultSet rs = db.bagContents(0, clientID);
		if (rs != null) {
			family_bag_pickup_panel.setData(rs, clientName, clientID);
		} else {
			showError("Database Error", "Failed to load pickup data");
		}
		return family_bag_pickup_panel;
	}

	private static DropOffPanel getDropOffPanel() {
		drop_offs_panel = new DropOffPanel(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPanel(getHomePanel());
			}
		}, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Complete Drop Off");
				ArrayList<int[]> info = null;
				try {
					info = drop_offs_panel.getDropoffInfo();
				} catch (Exception e1) {
					showError("Input Error", "Invalid value for quantity. Please enter a number greater than 0.");
					e1.printStackTrace();
					return;
				}
				for (int[] arr : info) {
					System.out.println(Arrays.toString(arr));
					db.dropoff(arr[0], arr[1], arr[2]);
				}
				setPanel(getHomePanel());
			}
		});
		ResultSet sources = db.viewSources();
		ResultSet products = db.viewProducts();
		if (sources != null && products != null) {
			drop_offs_panel.setSources(sources, products);
		} else {
			showError("Database Error", "Failed to load pickup data");
		}
		return drop_offs_panel;
	}

	private static SearchClientPanel getSearchClientPanel() {
		if (search_client_panel == null) {
			search_client_panel = new SearchClientPanel(new ActionListener() { // New client
						public void actionPerformed(ActionEvent e) {
							setPanel(getNewClientPanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) { // add family
							setPanel(getAddFamilyPanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) { // return
							setPanel(getHomePanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) {// search
							String name = search_client_panel.getSearchName();
							String tele = search_client_panel.getSearchTelephone();
							System.out.println("Looking up " + name + " " + tele);
							ResultSet rs = db.searchClient(name, tele);
							if (rs != null) {
								search_client_panel.setData(rs);
							} else {
								showError("Database Error", "Failed to load data");
							}
						}
					});
		}
		ResultSet rs = db.searchClient("*", "*");
		if (rs != null) {
			search_client_panel.setData(rs);
		} else {
			showError("Database Error", "Failed to load data");
		}
		return search_client_panel;
	}

	private static NewClientPanel getNewClientPanel() {
		new_client_panel = new NewClientPanel(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object data[] = new_client_panel.getData();
				db.addClient((String) data[0], ((Integer) data[1]).intValue(), (String) data[2], (String) data[3], (String) data[4], (Date) data[5], (String) data[6], (Integer) data[7], (String) data[8], (String) data[9], (Integer) data[10], (String) data[11], (String) data[12], (Date) data[13], ((Integer) data[14]).intValue());
				setPanel(getSearchClientPanel());
			}
		});
		// ResultSet bagRS = db.listBags();
		// ResultSet finAidRS = db.viewFinAid();
		// if (bagRS != null && finAidRS != null) {
		// new_client_panel.setData(bagRS, finAidRS);
		// } else {
		// showError("Database Error", "Failed to load data");
		// }
		return new_client_panel;
	}

	private static AddFamilyPanel getAddFamilyPanel() {
		if (add_family_panel == null) {
			add_family_panel = new AddFamilyPanel(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int CID = search_client_panel.getSelectedCID();
					if (CID == -1) {
						showError("General Error", "No client selected!");
						setPanel(getSearchClientPanel());
						return;
					}
					Object[][] data = add_family_panel.getData();
					for (Object[] member : data) {
						db.addFamily(CID, (String) member[0], (String) member[1], (String) member[2], (Date) member[3]);
					}
					setPanel(getSearchClientPanel());
				}
			});
		}
		return add_family_panel;
	}

	private static BagListPanel getBagListPanel() {
		bag_list_panel = new BagListPanel(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Edit bag please");
				setPanel(getEditBagPanel(e.getID()));
			}
		}, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPanel(getHomePanel());
			}
		});
		ResultSet rs = db.viewBags();
		if (rs != null) {
			bag_list_panel.setData(rs);
		} else {
			showError("Database Error", "Failed to load data");
		}
		return bag_list_panel;
	}

	private static EditBagPanel getEditBagPanel(int bid) {
		edit_bag_panel = new EditBagPanel(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setPanel(getBagListPanel());
			}
		}, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Object arr[] : edit_bag_panel.getData()) {
					db.editBagProduct(edit_bag_panel.getBID(), (String) arr[0], Integer.parseInt((String) arr[1]));
					System.out.println("Sending " + Integer.parseInt((String) arr[1]));
				}
				setPanel(getBagListPanel());
			}
		});
		ResultSet products = db.bagContents(bid);
		if (products != null) {
			edit_bag_panel.setData(products, bid);
		} else {
			showError("Database Error", "Failed to load pickup data");
		}
		return edit_bag_panel;
	}

	private static ProductListPanel getProductListPanel() {
		if (product_list_panel == null) {
			product_list_panel = new ProductListPanel(new ActionListener() { // return
						public void actionPerformed(ActionEvent e) {
							setPanel(getHomePanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) { // add
							setPanel(getNewProductPanel());
						}
					}, new ActionListener() { // search
						public void actionPerformed(ActionEvent arg0) {
							ResultSet rs = db.listProducts(product_list_panel.searchString());
							if (rs != null) {
								product_list_panel.setData(rs);
							} else {
								showError("Database Error", "Failed to load pickup data");
							}
						}
					}, new ActionListener() { // all
						public void actionPerformed(ActionEvent e) {
							ResultSet rs = db.listProducts(null);
							if (rs != null) {
								product_list_panel.setData(rs);
							} else {
								showError("Database Error", "Failed to load pickup data");
							}
						}
					});
		}
		ResultSet rs = db.listProducts(null);
		if (rs != null) {
			product_list_panel.setData(rs);
		} else {
			showError("Database Error", "Failed to load pickup data");
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
					Object[] data = new_product_panel.getData();
					String name = (String) data[0];
					int sid = (Integer) data[1];
					double cost = Double.parseDouble((String) data[2]);
					System.out.println("Saving new product: " + name + " " + sid + " " + cost);
					db.addProduct(name, sid, cost);
					setPanel(getProductListPanel());
				}
			});
		}
		ResultSet rs = db.viewSources();
		if (rs != null) {
			new_product_panel.setSources(rs);
		} else {
			showError("Database Error", "Failed to load pickup data");
		}
		return new_product_panel;
	}

	private static MSRPanel getMSRPanel() {
		if (msr_panel == null) {
			msr_panel = new MSRPanel(new ActionListener() { // return
						public void actionPerformed(ActionEvent e) {
							setPanel(getHomePanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) { // grocery
							setPanel(getGroceryPanel());
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) { // active
							ResultSet rs = db.msr(username, false);
							if (rs != null) {
								msr_panel.setData(rs);
							} else {
								showError("Database Error", "Failed to load msr data. Insufficient privilege?");
							}
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent e) { // past
							ResultSet rs = db.msr(username, true);
							if (rs != null) {
								msr_panel.setData(rs);
							} else {
								showError("Database Error", "Failed to load msr data. Insufficient privilege?");
							}
						}
					});
		}
		ResultSet rs = db.msr(username, false);
		if (rs != null) {
			msr_panel.setData(rs);
		} else {
			showError("Database Error", "Failed to load msr data. Insufficient privilege?");
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
		ResultSet rs = db.groceryReport(username);
		if (rs != null) {
			grocery_list_panel.setData(rs);
		} else {
			showError("Database Error", "Failed to load grocery report. Insufficient privilege?");
		}
		return grocery_list_panel;
	}

	private static void showError(String title, String errorMessage) {
		JOptionPane.showMessageDialog(applicationFrame, errorMessage, title, JOptionPane.ERROR_MESSAGE);
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
