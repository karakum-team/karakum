type ArrayAlias = Array<string>
type RecordAlias = Record<string, string>
type ErrorAlias = Error
type IterableAlias = Iterable<string>
type MapAlias = Map<string, string>
type SetAlias = Set<string>

interface CustomRecord extends Record<string, string> {}
interface CustomIterable extends Iterable<string> {}
interface CustomMap extends Map<string, string> {}
interface CustomSet extends Set<string> {}
