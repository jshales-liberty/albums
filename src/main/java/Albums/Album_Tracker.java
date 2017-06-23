package Albums;

import static spark.Spark.get;
import static spark.Spark.port;

import java.util.ArrayList;
import java.util.Iterator;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Album_Tracker {

	public static void main(String[] args) {

		ArrayList<Album> albums = new ArrayList<Album>();
		Album alb1 = new Album("Led Zeppelin IV", "Led Zeppelin", 1);
		albums.add(alb1);
		albums.add(new Album("For Emma", "Bon Iver", 2));
		albums.add(new Album("DAMN.", "Kendrick Lamar", 3));
		port(3000);

		get("/", (req, res) -> {

			JtwigTemplate template = JtwigTemplate
					.classpathTemplate("templates/albums.jtwig");
			JtwigModel model = JtwigModel.newModel().with("albums", albums);

			return template.render(model);
		});

		get("/json", (req, res) -> {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			return gson.toJson(albums);
		});

		get("/album/:id", (req, res) -> {
			for (int i = 0; i < albums.size(); i++) {
				if (albums.get(i).getId() == Integer
						.parseInt(req.params(":id"))) {
					return "Title: " + albums.get(i).getTitle()
							+ "<br> Artist: " + albums.get(i).getArtist();
				}
			}
			return null;
		});

		get("/create/", (req, res) -> {
			int newid = 0;
			for (int i = 0; i < albums.size(); i++) {
				boolean numberfound = false;
				if (albums.get(i).getId() == i) {
					numberfound = true;
				}
				if (numberfound) {
					newid = i;
					break;
				}
				newid = albums.size() + 1;
			}
			albums.add(new Album(req.queryParams("title"),
					req.queryParams("artist"), newid));
			res.redirect("/");
			return "";
		});

		get("/remove/:id", (req, res) -> {
			Iterator<Album> it = albums.iterator();
			while (it.hasNext()) {
				Album album = it.next();
				if (album.getId() == (Integer.parseInt(req.params(":id")))) {
					it.remove();
					return "album removed";
				}
			}
			return "no album found.";

		});

	}

}

class Album {

	private String title = "";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Album(String title, String artist, int id) {
		super();
		this.title = title;
		this.artist = artist;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String artist = "";
	private int id;

}