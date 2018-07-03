package crm.service.restapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "picture")
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "picture_id")
	@JsonIgnore
	private long id;

    @Column(name = "filename", nullable = false)
    private String filename;

	@Column(name = "media_type", nullable = false)
	private String mediaType;

    @Column(name = "url", nullable = false)
	private String url;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
