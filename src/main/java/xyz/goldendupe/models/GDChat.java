package xyz.goldendupe.models;

import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.utils.MemberType;

public enum GDChat {
	GLOBAL(null, MemberType.DEFAULT),
	STAFF(GoldenMessenger.MessageChannel.STAFF, MemberType.MODERATOR),
	ADMIN(GoldenMessenger.MessageChannel.ADMIN, MemberType.ADMINISTRATOR),
	OG(GoldenMessenger.MessageChannel.OG, MemberType.OG),
	DONATOR(GoldenMessenger.MessageChannel.DONATOR, MemberType.DONATOR),

	@Deprecated
	CLAN(null, null), // Maybe??

	;
	private final GoldenMessenger.MessageChannel channel;
	private final MemberType memberType;


	GDChat(GoldenMessenger.MessageChannel channel, MemberType memberType) {
		this.channel = channel;
		this.memberType = memberType;
	}

	@Nullable
	public GoldenMessenger.MessageChannel asMessageChannel(){
		return channel;
	}
	@Nullable
	public MemberType asMemberType() {
		return memberType;
	}
}
