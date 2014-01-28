package it.fdev.unisaconnect.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

	private String resultNumber, author, format, title, year;
	private String detailsUrl, positionUrl;

	public Book(String resultNumber, String author, String format, String title, String year, String detailsUrl, String positionUrl) {
		super();
		this.resultNumber = resultNumber;
		this.author = author;
		this.format = format;
		this.title = title;
		this.year = year;
		this.detailsUrl = detailsUrl;
		this.positionUrl = positionUrl;
	}

	public String getResultNumber() {
		return resultNumber;
	}

	public String getAuthor() {
		return author;
	}

	public String getFormat() {
		return format;
	}

	public String getTitle() {
		return title;
	}

	public String getYear() {
		return year;
	}

	public String getDetailsUrl() {
		return detailsUrl;
	}

	public String getPositionUrl() {
		return positionUrl;
	}

	public Book(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(resultNumber);
		dest.writeString(author);
		dest.writeString(format);
		dest.writeString(title);
		dest.writeString(year);
		dest.writeString(detailsUrl);
		dest.writeString(positionUrl);
	}

	private void readFromParcel(Parcel in) {
		resultNumber = in.readString();
		author = in.readString();
		format = in.readString();
		title = in.readString();
		year = in.readString();
		detailsUrl = in.readString();
		positionUrl = in.readString();
	}

	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}

		public Book[] newArray(int size) {
			return new Book[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
