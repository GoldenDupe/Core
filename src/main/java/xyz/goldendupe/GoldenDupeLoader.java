package xyz.goldendupe;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class GoldenDupeLoader implements PluginLoader {
	@Override
	public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {

		MavenLibraryResolver resolver = new MavenLibraryResolver();

		// Sonatype

		resolver.addRepository(new RemoteRepository.Builder("sonatype", "snapshots", "https://oss.sonatype.org/content/repositories/snapshots/").build());

		// Cloud
		String cloudFramework = "2.0.0-SNAPSHOT";
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-core:"+cloudFramework), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-paper:"+cloudFramework), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-brigadier:"+cloudFramework), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-minecraft-extras:"+cloudFramework), null));


		// Classgraph for reflections
		resolver.addDependency(new Dependency(new DefaultArtifact("io.github.classgraph:classgraph:4.8.165"), null));

		// Database stuff (mysql)
		resolver.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:5.1.0"), null));

//		resolver.addDependency(new Dependency(new DefaultArtifact()));

		pluginClasspathBuilder.addLibrary(resolver);
	}
}