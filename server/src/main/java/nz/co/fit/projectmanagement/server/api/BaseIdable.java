package nz.co.fit.projectmanagement.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseIdable implements Idable {
	private Long id;

	@Override
	@JsonProperty(required = false)
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
}
