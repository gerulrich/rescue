package net.cloudengine.utilities

class MongoCleanUp {
	
	static void main(def args) {
		println "borrando archivo de lock de mongo..."
		File f = new File("C:\\data\\db\\mongod.lock");
		if (f.exists()) {
			f.delete();
			println "archivo borrado"
		}
	}
}
