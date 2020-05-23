package aquarius0715.mysqlmain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MysqlMain extends JavaPlugin implements Listener {
    public String host, database, username, password;
    public int port;


    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // Plugin startup logic
        host = "localhost";
        port = 3306;
        database = "minecraftserver";
        username = "root";
        password = "mk871396";
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) throws SQLException, ClassNotFoundException {

        ItemStack[] items = {new ItemStack(Material.BREAD), new ItemStack(Material.PAPER)};

        if (event.getPlayer().hasPlayedBefore()) {
            for (Player on : Bukkit.getOnlinePlayers()) {
                on.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + event.getPlayer().getName() + "がログインしました");
                //複数回ログインしたことのある人の処理
                try {
                    //sqlの処理開始
                    Class.forName("com.mysql.jdbc.Driver");
                    //ここからSQLに接続する処理
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
                    if (!(conn.isClosed())) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SQL Connection successfully");
                    } else
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "SQL Connection failed");
                    //接続処理終了
                    ///////////////////////////////////
                    //UUIDとプレイヤー名をSQLにアップロード
                    //日付情報をSQLにアップロード
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy").format(date);
                    int Now_Year_Int = Integer.parseInt(format);
                    String format1 = new SimpleDateFormat("MM").format(date);
                    int Now_Month_Int = Integer.parseInt(format1);
                    String format2 = new SimpleDateFormat("dd").format(date);
                    int Now_Day_Int = Integer.parseInt(format2);
                    String sqlinsert = "INSERT INTO basetable (UUID, PlayerName, Year, Month, Day) VALUES ('" + event.getPlayer().getUniqueId().toString() + "' , '"
                            + event.getPlayer().getDisplayName() + "' , '"
                            + format + "' , '"
                            + format1 + "' , '"
                            + format2 + "');";
                    conn.createStatement().executeUpdate(sqlinsert);
                    ///////////////////////////////////
                    //SQLからデータを持ってくる処理
                    //前回ログインの時間を取得
                    String select_Year = "SELECT * FROM basetable where UUID = '" + event.getPlayer().getUniqueId() + "';";
                    String select_Month = "SELECT * FROM basetable where UUID = '" + event.getPlayer().getUniqueId() + "';";
                    String select_Day = "SELECT * FROM basetable where UUID = '" + event.getPlayer().getUniqueId() + "';";

                    ResultSet results_Year = conn.createStatement().executeQuery(select_Year);
                    results_Year.last();
                    ResultSet results_Month = conn.createStatement().executeQuery(select_Month);
                    results_Month.last();
                    ResultSet results_Day = conn.createStatement().executeQuery(select_Day);
                    results_Day.last();
                    String Year = results_Year.getString("Year");
                    int Year_Int = Integer.parseInt(Year);
                    String Month = results_Month.getString("Month");
                    int Month_Int = Integer.parseInt(Month);
                    String Day = results_Day.getString("Day");
                    int Day_Int = Integer.parseInt(Day);

                    //今回のログイン時間との差を計算しログインボーナスの配布
                    if (!(Now_Year_Int == Year_Int || Now_Month_Int == Month_Int || Now_Day_Int == Day_Int)) {
                        event.getPlayer().getInventory().addItem(items);
                        event.getPlayer().sendMessage("ログインボーナスをプレゼントしました");
                    } else
                        event.getPlayer().sendMessage("あなたはまだログインボーナスをもらえません。");
                    conn.close();
                    if (conn.isClosed()) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SQL Disconnection successfully");
                    } else
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "SQL Disconnection failed");
                    //sqlの処理終了
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            //初回ログインのプレイヤーの処理
        } else {
            for (Player on : Bukkit.getOnlinePlayers()) {

                try {
                    //sqlの処理開始
                    Class.forName("com.mysql.jdbc.Driver");
                    //ここからSQLに接続する処理
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
                    if (!(conn.isClosed())) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SQL Connection successfully");
                    } else
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "SQL Connection failed");
                    //接続処理終了
                    ///////////////////////////////////
                    //UUIDとプレイヤー名をSQLにアップロード
                    //日付情報をSQLにアップロード
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy").format(date);
                    String format1 = new SimpleDateFormat("MM").format(date);
                    String format2 = new SimpleDateFormat("dd").format(date);
                    String sqlinsert = "INSERT INTO basetable (UUID, PlayerName, Year, Month, Day) VALUES ('" + event.getPlayer().getUniqueId().toString() + "' , '"
                            + event.getPlayer().getDisplayName() + "' , '"
                            + format + "' , '"
                            + format1 + "' , '"
                            + format2 + "');";
                    conn.createStatement().executeUpdate(sqlinsert);

                    ///////////////////////////////////
                    //ログインボーナスの配布
                    event.getPlayer().getInventory().addItem(items);
                    Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + event.getPlayer().getDisplayName() + "さんが初めてサーバーにログインしました！");
                    event.getPlayer().sendMessage("ログインボーナスを配布しました！");
                    conn.close();
                    if (conn.isClosed()) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SQL Disconnection successfully");
                    } else
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "SQL Disconnection failed");
                    //sqlの処理終了
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}