package models.data

import java.io.File

import com.almworks.sqlite4java.{SQLiteStatement, SQLiteConnection}
import models.Sms.Message

class Sqlite3DB {

  val db = new SQLiteConnection(new File("~/devops/SMSender/database/dados.db"))

  def insertMessage(m: Message) {
    db.open(true);


    val insert_statement = db.prepare("INSERT INTO message VALUES (?, ?, ?, ?)");

    //id , origin, destination , message

    insert_statement.bind(0, m.from);
    insert_statement.bind(1, m.to);
    insert_statement.bind(2, m.message);

    insert_statement.step()

    db.dispose();

  }
}
